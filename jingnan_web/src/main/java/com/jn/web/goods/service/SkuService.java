package com.jn.web.goods.service;

import com.github.pagehelper.Page;
import com.jn.pojo.Sku;

import java.util.List;
import java.util.Map;

public interface SkuService {

    List<Sku> findBySpuId(String spuId);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Sku> findList(Map<String, Object> searchMap);

    /**
     * 根据sku库存id获取sku对象
     * @param id
     * @return
     */
    Sku findOneByskuId(String id);

    /**
     * 扣减库存, 增加销量
     * @param skuId 购买的库存id
     * @param num   购买数量
     */
    void decrCount(String skuId,Integer num);

    /**
     * 恢复库存, 恢复销量
     * @param skuId
     * @param num
     */
    void incrCount(String skuId,Integer num);

    /***
     * 查询所有
     * @return
     */
    List<Sku> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Sku findById(String id);

    /***
     * 新增
     * @param sku
     */
    void add(Sku sku);

    /***
     * 修改
     * @param sku
     */
    void update(Sku sku);

    /***
     * 删除
     * @param id
     */
    void delete(String id);


    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Sku> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Sku> findPage(Map<String, Object> searchMap, int page, int size);

}
