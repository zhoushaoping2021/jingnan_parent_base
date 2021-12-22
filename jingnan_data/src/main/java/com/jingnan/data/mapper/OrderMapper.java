package com.jingnan.data.mapper;

import com.jingnan.data.pojo.UserOrderMap;
import com.jn.pojo.Order;
import com.jn.pojo.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: sublun
 * @Date: 2021/5/21 15:55
 */
public interface OrderMapper extends Mapper<Order> {

    @Select("SELECT username,id orderId FROM `tb_order` where username = #{username} ")
    public List<UserOrderMap> getUserOrderList(String username);

}
