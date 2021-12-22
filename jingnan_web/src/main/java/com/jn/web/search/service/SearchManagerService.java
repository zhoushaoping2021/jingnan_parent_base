package com.jn.web.search.service;

import com.jn.entity.Result;

public interface SearchManagerService {

    /**
     * 创建索引库和映射关系
     */
    void createIndexAndMapping();


    /**
     * 删除索引库
     */
    void deleteIndex();

    /**
     * 根据spuId导入商品到ES中
     */
    void importBySpuId(String spuId);


    /**
     * 导入全部sku到ES中
     */
    void importAll();

    /**
     * 分页导入全部sku到ES中
     */
    void importAllByPage(int pageSize);

    /**
     * 根据spuId删除商品
     */
    void deleteBySpuId(String spuId);


    Result importBigAll();
    /**
     * 分页导入全部sku到ES中
     */
    Result importBigAllByPage(int pageSize);
    Result importBigAllByPage2(int pageSize);
    Result importBigAllByPage3(int pageSize);


}
