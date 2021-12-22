package com.jn.web.page.service;

import java.util.Map;

/**
 *
 */
public interface PageService {

    /**
     * 根据商品id, 获取商品详情页中需要的所有数据返回
     * @param spuId 商品id
     * @return
     */
    public Map<String, Object> findPageAllDataBySpuId(String spuId);

    /**
     * 根据商品的完整数据生成商品详情页面
     * @param dataMap
     * @throws Exception
     */
    public void createStaticPage(Map<String, Object> dataMap, String spuId) throws Exception;
}
