package com.jn.web.goods.service;

import java.util.Map;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-03
 */
public interface WxPayService {

    Map nativePay(String orderId);

    Map queryOrder(String orderId);

}
