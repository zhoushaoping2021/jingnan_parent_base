package com.jn.web.goods.service;

import com.jn.entity.Result;

import java.util.Map;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-01
 */
public interface CartService {

    Map<String, Object> list(String username);

    void addAndUpdate(String username, String skuId, Integer num);

    void del(String username, String skuId);

    void updateChecked(String username, String skuId, Boolean checked);

}
