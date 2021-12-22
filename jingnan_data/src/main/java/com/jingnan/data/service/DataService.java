package com.jingnan.data.service;

import com.jingnan.data.mapper.OrderItemMapper;
import com.jingnan.data.mapper.OrderMapper;
import com.jingnan.data.mapper.UserMapper;
import com.jingnan.data.pojo.UserOrderMap;
import com.jingnan.data.smapper.SOrderItemMapper;
import com.jingnan.data.smapper.SOrderMapper;
import com.jingnan.data.smapper.SUserMapper;
import com.jingnan.data.smapper.SUserOrderMapper;
import com.jn.entity.Result;
import com.jn.pojo.Order;
import com.jn.pojo.OrderItem;
import com.jn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.SQLException;
import java.util.List;


/**
 * @Author: sublun
 * 将本地数据库中的用户信息导入到分片数据库中
 */
@Service
public class DataService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SUserMapper sUserMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SOrderMapper sOrderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private SOrderItemMapper sOrderItemMapper;
    @Autowired
    private SUserOrderMapper sUserOrderMapper;

    public Result importUser() throws SQLException {
        //查询本地数据库中的用户列表
        List<User> userList = userMapper.selectAll();

        //将用户信息写入数据库集群
        userList.stream().forEach(u->sUserMapper.insert(u));
        //返回结果
        return Result.builder()
                .code(200)
                .flag(true)
                .message("数据导入完成")
                .build();
    }

    /**
     * 删除分片数据库中的所有用户
     * @return
     */
    public Result deleteAllUsers() {
        sUserMapper.deleteByExample(new Example(User.class));
        return Result.builder().flag(true).build();
    }

    /**
     * 从本地库中导入数据到sharding库，安装配置的分片规则进行数据分片
     * @return
     */
    public Result importAllData() throws SQLException {
        //1.导入用户数据
        importUserData();
        //导入订单表
        importOrders();
        //导入订单明细表
        importOrderItems();
        return Result.builder().flag(true).build();
    }

    /**
     * 导入订单数据
     */
    private void importOrders() {
        //查询得到列表
        List<Order> orderList = orderMapper.selectAll();
        //导入到分片库中
        orderList.forEach(o->sOrderMapper.insert(o));
    }

    /**
     * 导入订单明细
     */
    private void importOrderItems() {
        List<OrderItem> orderItems = orderItemMapper.selectAll();
        orderItems.forEach(oi->sOrderItemMapper.insert(oi));
    }

    private void importUserData() {
        //查询本地数据库中的用户列表
        List<User> userList = userMapper.selectAll();

        //将用户信息写入数据库集群
        userList.stream().forEach(u->{
            //插入用户数据
            sUserMapper.insert(u);
            //同时插入用户订单对应表
            //importUserOrder(u.getUsername());
        });
    }

    /**
     * 导入用户订单异构表数据
     */
    private void importUserOrder(String username) {
        //查询订单表找到用户和订单的对应关系
        List<UserOrderMap> userOrderList = orderMapper.getUserOrderList(username);
        //将对应关系写入异构表
        userOrderList.forEach(e->sUserOrderMapper.insert(e));
    }

    public Result clearAll() {
        sUserMapper.deleteByExample(new Example(User.class));
        sOrderMapper.deleteByExample(new Example(Order.class));
        sOrderItemMapper.deleteByExample(new Example(OrderItem.class));
        sUserOrderMapper.deleteByExample(new Example(UserOrderMap.class));
        return Result.builder().flag(true).build();
    }



}

