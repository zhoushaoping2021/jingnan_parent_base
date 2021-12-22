package com.jingnan.data.service;

import com.jingnan.data.mapper.SkuBigMapper;
import com.jingnan.data.mapper.SkuMapper;
import com.jingnan.data.pojo.SkuBig;
import com.jn.entity.Result;
import com.jn.pojo.Sku;
import com.jn.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.spring.annotation.MapperScan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Author: sublun
 * @Date: 2021/2/8 16:43
 */
//@Service
@Slf4j
public class SkuDataService {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuBigMapper skuBigMapper;
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ExecutorService executorService;

    @Transactional
    public Result buildData(int count) {
        log.info("循环次数：" + count);
        log.info("插入数据，开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            log.info("执行第【" + i + "】次循环");
            log.info("查询数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            List<Sku> skuList = skuMapper.selectAll();
            log.info("查询数据结束：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            log.info("");
            log.info("插入数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            skuList.forEach(sku -> {
                long id = idWorker.nextId();
                sku.setId(id + "");
                SkuBig skuBig = new SkuBig();
                BeanUtils.copyProperties(sku, skuBig);
                skuBigMapper.insert(skuBig);
            });
            log.info("插入数据【"+skuList.size()+"】行：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        }
        log.info("插入数据完成：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        long endTime = System.currentTimeMillis();
        long costTime = (endTime - startTime) / 1000;
        System.out.println("插入耗时：" + costTime + "s");
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + costTime + "s")
                .build();
    }


    /**
     * 循环45次
     * Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "http-nio-8080-BlockPoller"
     *
     * Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "HikariPool-1 housekeeper"
     *
     * Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "http-nio-8080-ClientPoller"
     * Exception in thread "mysql-cj-abandoned-connection-cleanup" java.lang.OutOfMemoryError: Java heap space
     * 2021-02-08 20:01:35.361 ERROR 20220 --- [alina-utility-1] org.apache.catalina.core.ContainerBase   : Exception processing background thread
     *
     *      * java.util.concurrent.ExecutionException: java.lang.OutOfMemoryError: Java heap space
     *      * 	at java.base/java.util.concurrent.FutureTask.report(FutureTask.java:122) ~[na:na]
     *      * 	at java.base/java.util.concurrent.FutureTask.get(FutureTask.java:191) ~[na:na]
     *      * 	at org.apache.catalina.core.ContainerBase.threadStart(ContainerBase.java:1276) ~[tomcat-embed-core-9.0.37.jar:9.0.37]
     *      * 	at org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessorMonitor.run(ContainerBase.java:1322) ~[tomcat-embed-core-9.0.37.jar:9.0.37]
     *      * 	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
     *      * 	at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305) ~[na:na]
     *      * 	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305) ~[na:na]
     *      * 	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130) ~[na:na]
     *      * 	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630) ~[na:na]
     *      * 	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.37.jar:9.0.37]
     *      * 	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
     * Caused by: java.lang.OutOfMemoryError: Java heap space
     *
     * 2021-02-08 20:01:35.373 ERROR 20220 --- [nio-8080-exec-5] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Handler dispatch failed; nested exception is java.lang.OutOfMemoryError: Java heap space] with root cause
     *
     * java.lang.OutOfMemoryError: Java heap space
     * @param count
     * @return
     */
    @Transactional
    public Result buildDataBatch(int count) {
        log.info("循环次数：" + count);
        log.info("插入数据，开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        SkuBigMapper skuBigMapper = sqlSession.getMapper(SkuBigMapper.class);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            log.info("执行第【" + i + "】次循环");
            log.info("查询数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            List<Sku> skuList = skuMapper.selectAll();
            log.info("查询数据结束：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            log.info("插入数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            skuList.forEach(sku -> {
                long id = idWorker.nextId();
                sku.setId(id + "");
                SkuBig skuBig = new SkuBig();
                BeanUtils.copyProperties(sku, skuBig);
                skuBigMapper.insert(skuBig);
            });
            log.info("插入数据【"+skuList.size()+"】行：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        }
        log.info("插入数据完成：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        long endTime = System.currentTimeMillis();
        long costTime = (endTime - startTime) / 1000;
        System.out.println("插入耗时：" + costTime + "s");
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + costTime + "s")
                .build();
    }

    public Result buildDataBatch2(int count) {
        log.info("循环次数：" + count);
        log.info("插入数据，开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        SkuBigMapper skuBigMapper = sqlSession.getMapper(SkuBigMapper.class);
        long startTime = System.currentTimeMillis();
        log.info("查询数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        List<Sku> skuList = skuMapper.selectAll();
        log.info("查询数据结束：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        List<SkuBig> skuBigList = skuList.stream().map(sku -> {
            SkuBig skuBig = new SkuBig();
            BeanUtils.copyProperties(sku, skuBig);
            return skuBig;
        }).collect(Collectors.toList());
        for (int i = 0; i < count; i++) {
            log.info("执行第【" + i + "】次循环");

            log.info("插入数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            skuBigList.forEach(e -> {
                long id = idWorker.nextId();
                e.setId(id + "");
                skuBigMapper.insert(e);
            });
            log.info("插入数据【"+skuList.size()+"】行：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            //提交一次数据
            sqlSession.commit();
        }
        log.info("插入数据完成：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        long endTime = System.currentTimeMillis();
        long costTime = (endTime - startTime) / 1000;
        System.out.println("插入耗时：" + costTime + "s");
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + costTime + "s")
                .build();
    }

    public Result buildDataBatch3(int count) {
        log.info("循环次数：" + count);
        log.info("插入数据，开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        SkuBigMapper skuBigMapper = sqlSession.getMapper(SkuBigMapper.class);
        long startTime = System.currentTimeMillis();
        log.info("查询数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        List<Sku> skuList = skuMapper.selectAll();
        log.info("查询数据结束：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        List<SkuBig> skuBigList = skuList.stream().map(sku -> {
            SkuBig skuBig = new SkuBig();
            BeanUtils.copyProperties(sku, skuBig);
            return skuBig;
        }).collect(Collectors.toList());
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Future<String> future = executorService.submit(() -> {
                log.info("插入数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
                skuBigList.forEach(e -> {
                    long id = idWorker.nextId();
                    e.setId(id + "");
                    skuBigMapper.insert(e);
                });
                log.info("插入数据【" + skuList.size() + "】行：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
                //提交一次数据
                sqlSession.commit();
            }, "data-task-" + i);
            futures.add(future);
        }
        /*new Thread(()->{
            while (true) {
                boolean flag = true;
                for (int i = 0; i < futures.size(); i++) {
                    Future future = futures.get(i);
                    if (!future.isDone()) {
                        flag = false;
                        log.debug("检查线程第【"+i+"】个线程是否完成：" + future.isDone());
                    } else {
                        Object result = null;
                        try {
                            result = future.get(10, TimeUnit.MILLISECONDS);
                            if (result != null) {
                                System.out.println("检查结果：" + result + "完成");
                                futures.remove(future);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (flag) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();*/
        log.info("插入数据完成：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        long endTime = System.currentTimeMillis();
        long costTime = (endTime - startTime) / 1000;
        System.out.println("插入耗时：" + costTime + "s");
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + costTime + "s")
                .build();
    }

    public Result buildDataBatch4(int count) {
        log.info("循环次数：" + count);
        log.info("插入数据，开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));

        long startTime = System.currentTimeMillis();
        log.info("查询数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        List<Sku> skuList = skuMapper.selectAll();
        log.info("查询数据结束：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));

        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int num = i;
            Future<String> future = executorService.submit(() -> {
                SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
                SkuBigMapper skuBigMapper = sqlSession.getMapper(SkuBigMapper.class);
                log.info("task-"+ num +"插入数据开始：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
                List<SkuBig> skuBigList = skuList.stream().map(sku -> {
                    SkuBig skuBig = new SkuBig();
                    BeanUtils.copyProperties(sku, skuBig);
                    long id = idWorker.nextId();
                    skuBig.setId(id + "");
                    return skuBig;
                }).collect(Collectors.toList());
                skuBigList.forEach(e -> {
                    skuBigMapper.insert(e);
                });
                log.info("task-"+ num +"插入数据【" + skuList.size() + "】行：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
                skuBigList = null;
                //System.gc();
                //提交一次数据
                sqlSession.commit(true);
                sqlSession.close();
                log.info("task-"+ num +"完成：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            }, "data-task-" + i);
            futures.add(future);
        }
        new Thread(()->{
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
                        log.info("检查线程第【"+i+"】个线程是否完成：" + future.isDone());
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
        log.info("插入数据完成：" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        long endTime = System.currentTimeMillis();
        long costTime = (endTime - startTime) / 1000;
        System.out.println("插入耗时：" + costTime + "s");
        return Result.builder()
                .code(200)
                .flag(true)
                .data("耗时" + costTime + "s")
                .build();
    }


}
