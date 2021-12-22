package com.jn.web.goods.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.jn.entity.Constants;
import com.jn.pojo.Order;
import com.jn.web.goods.service.OrderService;
import com.jn.web.goods.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-03
 */
@Service
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private WXPay wxPay;

    @Autowired
    OrderService orderService;

    @Value("${wxpay.notify_url}")
    private String notifyUrl;

    @Override
    public Map nativePay(String orderId) {
       /* Order order = (Order)
                redisTemplate.boundValueOps(Constants.REDIS_ORDER_PAY + userName).get();*/
        Order order=orderService.findById(orderId);
        try {
            //1.封装请求参数
            Map<String,String> map = new HashMap();
            map.put("body","京南商城商城");//商品描述
            map.put("out_trade_no",order.getId());//订单号
            map.put("total_fee","1");//金额
            map.put("spbill_create_ip","127.0.0.1");//终端IP
            map.put("notify_url",notifyUrl);//回调地址,先随便填一个
            map.put("trade_type","NATIVE");//交易类型
            Map<String, String> mapResult = wxPay.unifiedOrder( map ); //调 用统一下单
            mapResult.put("orderId",order.getId());
            mapResult.put("payMoney",String.valueOf(order.getPayMoney()));
            return mapResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Map queryOrder(String orderId) {
        Map map=new HashMap( );
        map.put("out_trade_no", orderId);
        try {
            return wxPay.orderQuery(map);
        } catch (Exception e) {
            e.printStackTrace();
            return null; }
    }

}
