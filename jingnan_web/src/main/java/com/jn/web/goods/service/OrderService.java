package com.jn.web.goods.service;

import com.jn.pojo.Order;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-02
 */
public interface OrderService {

    Boolean add(Order order);

    Order findById(String orderId);

    void updateOrderPayStatus(String orderId, String transactionId);
}
