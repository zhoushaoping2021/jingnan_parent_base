package com.jn.web.goods.service.impl;

import com.jn.pojo.Order;
import com.jn.pojo.OrderItem;
import com.jn.util.IdWorker;
import com.jn.web.goods.dao.OrderItemMapper;
import com.jn.web.goods.dao.OrderMapper;
import com.jn.web.goods.service.CartService;
import com.jn.web.goods.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-02
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    CartService cartService;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public Boolean add(Order order) {
        //查询出用户的所有购物车
        Map orderItemMap = cartService.list(order.getUsername()); //统计计算
        Integer totalMoney = Integer.parseInt(String.valueOf(orderItemMap.get("totalPrice")));
        Integer num = Integer.parseInt(String.valueOf(orderItemMap.get("totalNum"))); //购买商品数量
        order.setTotalNum(num); //购买金额
        order.setTotalMoney(totalMoney); //支付金额
        order.setPayMoney(totalMoney);
        //优惠金额
        order.setPreMoney(totalMoney);
        //其他数据完善
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0"); //0:未评价，1：已评价
        order.setSourceType("1"); //来源，1：WEB
        order.setOrderStatus("0"); //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0"); //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0"); //0:未发货，1：已发货，2：已收货
        order.setId(String.valueOf(idWorker.nextId()));
        orderMapper.insertSelective(order);
        //添加订单明细
        List<OrderItem> orderItemList = (List<OrderItem>)orderItemMap.get("orderItemList");
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(String.valueOf(idWorker.nextId()));
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }//清除Redis缓存购物车数据
        redisTemplate.delete("Cart_"+order.getUsername());
        return true;
    }

    @Override
    public Order findById(String orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public void updateOrderPayStatus(String orderId, String transactionId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order!=null && "0".equals(order.getPayStatus())){
            //存在订单且状态为0
            order.setPayStatus("1");
            order.setOrderStatus("1");
            order.setUpdateTime(new Date());
            order.setPayTime(new Date());
            //微信返回的交易流水号
            order.setTransactionId(transactionId);
            orderMapper.updateByPrimaryKeySelective(order);
            //记录订单变动日志
            /*OrderLog orderLog=new OrderLog();
            orderLog.setId( idWorker.nextId()+"" );
            orderLog.setOperater("system");// 系统
            orderLog.setOperateTime(new Date());//当前日期
            orderLog.setOrderStatus("1");
            orderLog.setPayStatus("1");
            orderLog.setRemarks("支付流水号"+transactionId);
            orderLog.setOrderId(order.getId());
            orderLogMapper.insertSelective(orderLog); */
        }
    }


}
