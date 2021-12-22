package com.jn.entity;

/**
 * 常量
 *
 */
public class Constants {
    //购物车
    public static final String REDIS_CART = "cart_";

    //待支付订单key
    public final static String REDIS_ORDER_PAY = "order_pay_";
    //秒杀商品key
    public static final String SECKILL_GOODS_KEY="seckill_goods_";
    //秒杀商品库存数key
    public static final String SECKILL_GOODS_STOCK_COUNT_KEY="seckill_goods_stock_count_";
    //秒杀用户key
    public static final String SECKILL_USER_KEY = "seckill_user_";
    //秒杀隐藏下单接口, 页面令牌前缀
    public static final String PAGE_TOKEN = "PAGE_TOKEN_";

    /**
     * 商品上架交换机
     */
    public static final String GOODS_UP_EXCHANGE = "goods_up_exchange";

    /**
     * 商品下架交换机
     */
    public static final String GOODS_DOWN_EXCHANGE = "goods_down_exchange";

    //未支付
    public static final String ORDER_STATUS_NOTPAY = "0";
    //已支付
    public static final String ORDER_STATUS_PAY = "1";
    //未发货
    public static final String ORDER_STATUS_NOTCONSING = "2";
    //已发货
    public static final String ORDER_STATUS_CONSING = "3";
    //已收货
    public static final String ORDER_STATUS_RECIVE = "4";
    //订单完成
    public static final String ORDER_STATUS_END = "5";
    //订单关闭
    public static final String ORDER_STATUS_CLOSE = "6";
}
