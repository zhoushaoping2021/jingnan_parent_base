package com.jn.web.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.jn.entity.Page;
import com.jn.pojo.*;
import com.jn.util.IdWorker;
import com.jn.web.goods.dao.*;
import com.jn.web.goods.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * @Author yaxiongliu
 **/
@Service
@Slf4j
public class SpuServiceImpl implements SpuService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBrandMpper categoryBrandMpper;


    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id){
        //直接从数据库查询
        //主键查询：CPU不耗时操作
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //模拟程序耗时操作，如果方法是比较耗时操作，性能优化就非常有必要
        try {
            TimeUnit.SECONDS.sleep(1);

            log.info("模拟耗时操作，睡眠1s时间");
            log.info("对象占用JVM堆内存大小：{}"+ RamUsageEstimator.sizeOfObject(spu));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return spu;
    }


    /**
     * 增加
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }


    /**
     * 修改
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Spu>)spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Spu>)spuMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 主键
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
            }
            // 货号
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andEqualTo("sn",searchMap.get("sn"));
            }
            // SPU名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 副标题
            if(searchMap.get("caption")!=null && !"".equals(searchMap.get("caption"))){
                criteria.andLike("caption","%"+searchMap.get("caption")+"%");
            }
            // 图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
            }
            // 图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
            }
            // 售后服务
            if(searchMap.get("saleService")!=null && !"".equals(searchMap.get("saleService"))){
                criteria.andLike("saleService","%"+searchMap.get("saleService")+"%");
            }
            // 介绍
            if(searchMap.get("introduction")!=null && !"".equals(searchMap.get("introduction"))){
                criteria.andLike("introduction","%"+searchMap.get("introduction")+"%");
            }
            // 规格列表
            if(searchMap.get("specItems")!=null && !"".equals(searchMap.get("specItems"))){
                criteria.andLike("specItems","%"+searchMap.get("specItems")+"%");
            }
            // 参数列表
            if(searchMap.get("paraItems")!=null && !"".equals(searchMap.get("paraItems"))){
                criteria.andLike("paraItems","%"+searchMap.get("paraItems")+"%");
            }
            // 是否上架
            if(searchMap.get("isMarketable")!=null && !"".equals(searchMap.get("isMarketable"))){
                criteria.andEqualTo("isMarketable",searchMap.get("isMarketable"));
            }
            // 是否启用规格
            if(searchMap.get("isEnableSpec")!=null && !"".equals(searchMap.get("isEnableSpec"))){
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
            }
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
            }
            // 审核状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
            }

            // 品牌ID
            if(searchMap.get("brandId")!=null ){
                criteria.andEqualTo("brandId",searchMap.get("brandId"));
            }
            // 一级分类
            if(searchMap.get("category1Id")!=null ){
                criteria.andEqualTo("category1Id",searchMap.get("category1Id"));
            }
            // 二级分类
            if(searchMap.get("category2Id")!=null ){
                criteria.andEqualTo("category2Id",searchMap.get("category2Id"));
            }
            // 三级分类
            if(searchMap.get("category3Id")!=null ){
                criteria.andEqualTo("category3Id",searchMap.get("category3Id"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
            // 运费模板id
            if(searchMap.get("freightId")!=null ){
                criteria.andEqualTo("freightId",searchMap.get("freightId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }


    @Transactional
    @Override
    public void addGoods(Goods goods) {
        //1.保存spu
        goods.getSpu().setId(String.valueOf(idWorker.nextId()));//雪花算法生成主键ID
        spuMapper.insertSelective(goods.getSpu());
        //2.保存sku列表
        saveSkuList(goods);
    }

    private void saveSkuList(Goods goods) {
        Spu spu = goods.getSpu();
        Integer brandId = spu.getBrandId(); //品牌ID
        Integer category3Id = spu.getCategory3Id(); //三级ID
        if(brandId==null || brandId==0){
            throw  new RuntimeException("品牌ID不能为空");
        }
        if(category3Id==null || category3Id==0){
            throw  new RuntimeException("分类ID不能为空");
        }

        //通过品牌ID查询品牌
        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        //通过分类ID查询三级分类
        Category category = categoryMapper.selectByPrimaryKey(category3Id);

        //处理分类与品牌之间的关系
        CategoryBrand cond = new CategoryBrand();
        cond.setCategoryId(category3Id);
        cond.setBrandId(brandId);
        int count = categoryBrandMpper.selectCount(cond);
        if(count==0){//如果根据ID查不到记录，那么新增数据
            categoryBrandMpper.insertSelective(cond);
        }

        List<Sku> skuList = goods.getSkuList();
        if(skuList!=null && skuList.size()>0){
            for (Sku sku : skuList) {
                sku.setId(String.valueOf(idWorker.nextId()));//雪花算法生成主键ID
                if(StringUtils.isEmpty(sku.getSpec())){
                    sku.setSpec("{}");
                }
                String skuName = spu.getName();
                Map<String,String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
                for(String key : specMap.keySet()){
                    String val = specMap.get(key);
                    skuName += " " + val;
                }
                sku.setName(skuName);//名称构成规则：spu的名称 + sku的规格的值拼接的结果（通过空格进行分割）
                sku.setCreateTime(new Date());//创建时间
                sku.setUpdateTime(new Date());//更新时间
                sku.setSpuId(spu.getId());
                if(category!=null){
                    sku.setCategoryId(category3Id);//三级分类ID
                    sku.setCategoryName(category.getName());//三级分类名称
                }
                if(brand!=null){
                    sku.setBrandName(brand.getName());//品牌名称
                }
                skuMapper.insertSelective(sku);
            }
        }
    }


    @Override
    public Goods findBySpuId(String spuId) {
        //1.根据spuId查询到spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.根据spuId查询到sku列表
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);

        //3.封装goods
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);

        return goods;
    }


    @Transactional
    @Override
    public void updateGoods(Goods goods) {
        //1.更新spu
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);

        //2.根据spuId删除sku列表
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        skuMapper.delete(sku);

        //3.新增sku列表数据
        saveSkuList(goods);
    }


    @Override
    public void auditGoods(String spuId) {
        //1.根据spuId查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.判断spu是否存在，如果不存在就抛异常
        if(spu==null){
            throw new RuntimeException("spu不存在！");
        }

        //3.判断spu状态是否是未审核
        if(!"0".equals(spu.getStatus())){
            throw new RuntimeException("spu状态不对");
        }

        //4.设置状态为审核通过，然后更新
        Spu spuCon = new Spu();
        spuCon.setId(spuId);
        spuCon.setStatus("1");
        spuMapper.updateByPrimaryKeySelective(spuCon);
    }


    @Override
    public void upGoods(String spuId) {
        //1.根据spuId查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.判断spu是否存在，如果不存在就抛异常
        if(spu==null){
            throw new RuntimeException("spu不存在！");
        }

        //3.如果商品未审核通过是不能上架的
        if(!"1".equals(spu.getStatus())){
            throw new RuntimeException("spu未审核不能上架");
        }

        //4.如果商品已经上架过是不能再次上架的
        if(!"0".equals(spu.getIsMarketable())){
            throw new RuntimeException("spu已经上架不能再次上架");
        }

        //5.如果商品已被逻辑删除不能上架
        if(!"0".equals(spu.getIsDelete())){
            throw new RuntimeException("spu已被删除不能上架");
        }

        //6.设置状态为已上架，然后更新
        Spu spuCon = new Spu();
        spuCon.setId(spuId);
        spuCon.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spuCon);

        //7.将spuId存入mq ,mq的消费者负责根据spuId查询出商品列表然后导入ES
        //rabbitTemplate.convertAndSend(Constants.GOODS_UP_EXCHANGE,null,spuId);
    }


    @Override
    public void downGoods(String spuId) {
        //1.根据spuId查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.判断spu是否存在，如果不存在就抛异常
        if(spu==null){
            throw new RuntimeException("spu不存在！");
        }

        //3.如果商品已经下架就不能再次下架
        if(!"1".equals(spu.getIsMarketable())){
            throw new RuntimeException("spu已经下架不能再次下架");
        }

        //4.设置状态为已下架，然后更新
        Spu spuCon = new Spu();
        spuCon.setId(spuId);
        spuCon.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spuCon);

        //5.将spuId存入rabbitmq，MQ监听器负责将数据从ES中删除
        //rabbitTemplate.convertAndSend(Constants.GOODS_DOWN_EXCHANGE,null,spuId);
    }


    @Override
    public void deleteLogic(String spuId) {
        //1.根据spuId查询spu
        Spu spu = deleteCondition(spuId);

        //4.如果商品已经被逻辑删除过则不能删除
        if(!"0".equals(spu.getIsDelete())){
            throw new RuntimeException("spu已经被删除过无需再次删除");
        }

        //5.设置状态为已逻辑删除，然后更新
        Spu spuCon = new Spu();
        spuCon.setId(spuId);
        spuCon.setIsDelete("1");
        spuMapper.updateByPrimaryKeySelective(spuCon);
    }

    private Spu deleteCondition(String spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.判断spu是否存在，如果不存在就抛异常
        if(spu==null){
            throw new RuntimeException("spu不存在！");
        }

        //3.如果商品在上架中，则不能删除
        if(!"0".equals(spu.getIsMarketable())){
            throw new RuntimeException("spu必须下架才能删除");
        }
        return spu;
    }

    @Override
    public void restore(String spuId) {
        //1.根据spuId查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.判断spu是否存在，如果不存在就抛异常
        if(spu==null){
            throw new RuntimeException("spu不存在！");
        }

        //3.如果商品已经恢复就不需要再次恢复
        if(!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("spu已经恢复过无需再次恢复");
        }

        //4.设置状态为已恢复，然后更新
        Spu spuCon = new Spu();
        spuCon.setId(spuId);
        spuCon.setIsDelete("0");
        spuMapper.updateByPrimaryKeySelective(spuCon);
    }


    @Override
    public void deleteGoods(String spuId) {
        //1.根据spuId查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //2.判断spu是否存在，如果不存在就抛异常
        if(spu==null){
            throw new RuntimeException("spu不存在！");
        }

        //3.如果商品在上架中，则不能删除
        if(!"0".equals(spu.getIsMarketable())){
            throw new RuntimeException("spu必须下架才能删除");
        }

        //4.执行物理删除
        //删除spu
        spuMapper.deleteByPrimaryKey(spuId);

        //删除对应的所有sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        skuMapper.delete(sku);
    }
}
