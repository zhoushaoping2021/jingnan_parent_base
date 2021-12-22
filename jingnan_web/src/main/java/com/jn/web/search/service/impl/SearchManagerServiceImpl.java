package com.jn.web.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.jn.entity.Result;
import com.jn.pojo.Sku;
import com.jn.web.goods.service.SkuService;
import com.jn.web.search.mapper.SkuBigMapper;
import com.jn.web.search.pojo.SkuBig;
import com.jn.web.search.pojo.SkuInfo;
import com.jn.web.search.repostory.SearchRepository;
import com.jn.web.search.service.SearchManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchManagerServiceImpl implements SearchManagerService {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuBigMapper skuBigMapper;

    @Autowired
    private ExecutorService executorService;

    @Override
    public void createIndexAndMapping() {
        IndexOperations ops = template.indexOps(SkuInfo.class);
        if (!ops.exists()) {
            template.indexOps(SkuInfo.class).create();
        }
    }

    @Override
    public void deleteIndex() {
        template.indexOps(SkuInfo.class).delete();

    }


    @Override
    public void importBySpuId(String spuId) {
        //根据spuId查询到sku列表（通过feign调用）
        Sku sku = skuService.findById(spuId);
        //将sku转换成ES文档对应的SkuInfo
        SkuInfo skuInfo = convertSkuInfo(sku);
        //将sku数据导入到es中
        searchRepository.save(skuInfo);
    }

    private SkuInfo convertSkuInfo(Sku sku) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(sku, skuInfo);
        String specJSON = sku.getSpec();
        Map specMap = JSON.parseObject(specJSON, Map.class);
        skuInfo.setSpecMap(specMap);
        //设置价格
        skuInfo.setPrice(sku.getPrice().longValue());
        return skuInfo;
    }

    private SkuInfo convertSkuInfo(SkuBig sku) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(sku, skuInfo);
        String specJSON = sku.getSpec();
        Map specMap = JSON.parseObject(specJSON, Map.class);
        skuInfo.setSpecMap(specMap);
        //设置id
        skuInfo.setId(sku.getId().toString());
        //设置价格
        skuInfo.setPrice(sku.getPrice().longValue());
        return skuInfo;
    }


    @Override
    public void importAll() {
        //1.查询全部的sku列表数据
        List<Sku> skuList = skuService.findAll();

        //2.将列表转为JSON字符串
        List<SkuInfo> skuInfoList = skuList.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());

        //3.导入全部数据
        searchRepository.saveAll(skuInfoList);
    }

    @Override
    public void importAllByPage(int pageSize) {
        Page<Sku> page = skuService.findPage(1, pageSize);

        //2.将列表转为JSON字符串
        List<SkuInfo> skuInfoList = page.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());

        //3.导入第一页数据
        searchRepository.saveAll(skuInfoList);

        //取总页数
        int pageCount = page.getPages();
        // 分页导入后续数据
        for (int i = 2; i <= pageCount; i++) {
            page = skuService.findPage(i, pageSize);
            //将列表转为JSON字符串
            skuInfoList = page.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());
            //导入一页数据
            searchRepository.saveAll(skuInfoList);
        }
    }


    @Override
    public void deleteBySpuId(String spuId) {
        //1.根据spuId查询到skuList
        List<Sku> skuList = skuService.findBySpuId(spuId);
        //2.执行删除
        for (Sku sku : skuList) {
            searchRepository.deleteById(Long.valueOf(sku.getId()));
        }
    }

    @Override
    public Result importBigAll() {
        Long startTime = System.currentTimeMillis();
        //1.查询全部的sku列表数据
        List<SkuBig> skuList = skuBigMapper.selectAll();
        log.info("查询全部数据完毕，耗时：" + (System.currentTimeMillis() - startTime) / 1000 + "s");
        Long curTime = System.currentTimeMillis();
        //2.将列表转为JSON字符串
        List<SkuInfo> skuInfoList = skuList.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());
        log.info("数据转换完毕，耗时：" + (System.currentTimeMillis() - curTime) / 1000 + "s");
        curTime = System.currentTimeMillis();
        //3.导入全部数据
        searchRepository.saveAll(skuInfoList);
        log.info("ES索引构建完毕，耗时：" + (System.currentTimeMillis() - curTime) / 1000 + "s");
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + (System.currentTimeMillis() - startTime) / 1000 + "s")
                .build();
    }


    /**
     * 将大表中的数据分页导入ES
     *
     * @param pageSize
     */
    @Override
    public Result importBigAllByPage(int pageSize) {
        long startTime = System.currentTimeMillis();
        //起始id
        long startId = 0;
        List<SkuBig> list = null;
        int counter = 1;
        List<Future> futures = new ArrayList<>();
        do {
            list = skuBigMapper.getListByPage(startId, pageSize);
            List<SkuBig> skuList = list;
            Future<String> future = executorService.submit(() -> {
                //2.将列表转为JSON字符串
                List<SkuInfo> skuInfoList = skuList.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());

                //3.导入全部数据
                searchRepository.saveAll(skuInfoList);
            }, "task" + counter + "已经完成");
            //取当前结果集中最大的id
            startId = list.get(list.size() - 1).getId();
            futures.add(future);
        } while (list.size() == pageSize);
        //监控线程
        new Thread(() -> {
            log.info("监视线程已经启动...");
            while (true) {
                boolean flag = true;
                int c = 0;
                for (int i = 0; i < futures.size(); i++) {
                    Future future = futures.get(i);
                    if (!future.isDone()) {
                        flag = false;
                        c++;
                    } else {
                        log.info("检查线程第【" + i + "】个线程是否完成：" + future.isDone());
                        Object result = null;
                        try {
                            result = future.get(10, TimeUnit.MILLISECONDS);
                            if (result != null) {
                                log.info("检查结果：" + result + "完成");
                                futures.remove(future);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                log.info("当前未完成任务数量：" + c);
                if (flag) {
                    break;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + (System.currentTimeMillis() - startTime) / 1000 + "s")
                .build();
    }

    /**
     * 将大表中的数据分页导入ES
     *
     * @param pageSize
     */
    @Override
    public Result importBigAllByPage2(int pageSize) {
        long startTime = System.currentTimeMillis();
        LinkedBlockingQueue<List<SkuBig>> taskQueue = new LinkedBlockingQueue<>(50);
        AtomicBoolean flag = new AtomicBoolean(true);
        AtomicBoolean mflag = new AtomicBoolean(true);
        Vector<Future> futures = new Vector<>();
        //producer
        new Thread(() -> {
            log.info("producer线程已经启动");
            //起始id
            long startId = 1;
            List<SkuBig> list = null;
            int counter = 1;
            do {
                list = skuBigMapper.getListByPage(startId, pageSize);
                log.info("查询出数据集：" + list.size());
                try {
                    taskQueue.put(list);
                    log.info("添加list到任务队列");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //取当前结果集中最大的id
                startId = list.get(list.size() - 1).getId();
                log.info("最大id" + startId);
            } while (list.size() == pageSize);
            //所有数据处理完毕，通知消费者可以撤了
            flag.set(false);
            log.info("所有数据查询完毕");
        }, "t_producer").start();
        //consumer
        new Thread(() -> {
            log.info("consumer线程已经启动");
            int counter = 0;
            while (flag.get() || !taskQueue.isEmpty()) {
                //从任务队列中取数据
                List<SkuBig> list = null;
                try {
                    list = taskQueue.take();
                    log.info("从队列中取出list");
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //处理数据，将数据导入ES
                List<SkuBig> finalList = list;
                log.info("将任务添加到线程池中处理：task-" + counter);
                //将任务放入线程池处理
                int finalCounter = counter;
                boolean tryReject = true;
                while(tryReject) {
                    try {
                        Future<String> future = executorService.submit(() -> {
                            //将列表转为JSON字符串
                            List<SkuInfo> skuInfoList = finalList.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());
                            //导入全部数据
                            searchRepository.saveAll(skuInfoList);
                            log.info("task-" + finalCounter + " 已经导入完成。");
                        }, "task-" + counter + "已经完成");
                        futures.add(future);
                        tryReject = false;
                    } catch (RejectedExecutionException e) {
                        log.info("线程池任务队列已满，拒绝提交任务。1s后重试");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                }
            }
            //监视线程关闭
            mflag.set(false);
            log.info("消费者处理完毕，关闭监视线程");
        }, "t_consumer").start();

        //监控线程
        new Thread(() -> {
            log.info("监视线程已经启动...");
            while (mflag.get()) {
                for (int i = 0; i < futures.size(); i++) {
                    Future future = futures.get(i);
                    log.info("检查线程第【" + i + "】个线程是否完成：" + future.isDone());
                    Object result = null;
                    try {
                        result = future.get(10, TimeUnit.MILLISECONDS);
                        if (result != null) {
                            log.info("检查结果：" + result + "完成");
                            futures.remove(future);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    log.info("当前未完成任务数量：" + futures.size());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            log.info("监视线程结束");
        }, "t_monitor").start();
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + (System.currentTimeMillis() - startTime) / 1000 + "s")
                .build();
    }

    /**
     * 将大表中的数据分页导入ES
     *
     * @param pageSize
     */
    @Override
    public Result importBigAllByPage3(int pageSize) {
        LinkedBlockingQueue<List<SkuBig>> taskQueue = new LinkedBlockingQueue<>(50);
        AtomicBoolean flag = new AtomicBoolean(true);
        AtomicBoolean mflag = new AtomicBoolean(true);
        Vector<Future> futures = new Vector<>();
        ElasticsearchRepositoryFactory factory = new ElasticsearchRepositoryFactory(template);
        //producer
        new Thread(() -> {
            log.info("producer线程已经启动");
            //起始id
            long startId = 1;
            List<SkuBig> list = null;
            int counter = 1;
            try {
                do {
                    list = skuBigMapper.getListByPage(startId, pageSize);
                    log.info("查询出数据集：" + list.size());
                    try {
                        taskQueue.put(list);
                        log.info("添加list到任务队列");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //取当前结果集中最大的id
                    startId = list.get(list.size() - 1).getId();
                    log.info("最大id:" + startId);
                } while (list.size() == pageSize);
            } catch (Exception e) {
                log.error("",e);
                e.printStackTrace();
            }
            //所有数据处理完毕，通知消费者可以撤了
            flag.set(false);
            log.info("所有数据查询完毕");
        }, "t_producer").start();
        //consumer
        new Thread(() -> {
            log.info("consumer线程已经启动");
            int counter = 0;
            while (flag.get() || !taskQueue.isEmpty()) {
                //从任务队列中取数据
                List<SkuBig> list = null;
                try {
                    list = taskQueue.take();
                    log.info("从队列中取出list");
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //处理数据，将数据导入ES
                List<SkuBig> finalList = list;
                log.info("将任务添加到线程池中处理：task-" + counter);
                //将任务放入线程池处理
                int finalCounter = counter;
                boolean tryReject = true;
                try {
                    while(tryReject) {
                        try {
                            Future<String> future = executorService.submit(() -> {
                                //将列表转为JSON字符串
                                List<SkuInfo> skuInfoList = finalList.stream().map(sku -> convertSkuInfo(sku)).collect(Collectors.toList());
                                SearchRepository repository = factory.getRepository(SearchRepository.class);
                                //System.out.println("repository:" + repository);
                                //导入全部数据
                                repository.saveAll(skuInfoList);
                                log.info("task-" + finalCounter + " 已经导入完成。");
                            }, "task-" + counter + "已经完成");
                            futures.add(future);
                            tryReject = false;
                        } catch (RejectedExecutionException e) {
                            log.info("线程池任务队列已满，拒绝提交任务。1s后重试");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("",e);
                    e.printStackTrace();
                }
            }
            //监视线程关闭
            mflag.set(false);
            log.info("消费者处理完毕，关闭监视线程");
        }, "t_consumer").start();

        //监控线程
        new Thread(() -> {
            log.info("监视线程已经启动...");
            long taskStartTime = System.currentTimeMillis();
            while (mflag.get() || futures.size() != 0) {
                for (int i = 0; i < futures.size(); i++) {
                    Future future = futures.get(i);
                    //log.info("检查线程第【" + i + "】个线程是否完成：" + future.isDone());
                    Object result = null;
                    try {
                        result = future.get(10, TimeUnit.MILLISECONDS);
                        if (result != null) {
                            log.info("检查结果：" + result + "完成");
                            futures.remove(future);
                        }
                    } catch (Exception e) {
                        /*log.error("",e);
                        e.printStackTrace();*/
                    }
                }
                log.info("当前未完成任务数量：" + futures.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("监视线程结束, 耗时：" + (System.currentTimeMillis() - taskStartTime) / 1000 + "s");
        }, "t_monitor").start();
        return Result.builder()
                .code(200)
                .flag(true)
                .build();
    }


}
