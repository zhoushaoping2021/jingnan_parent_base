package com.jingnan.data.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Author: sublun
 * @Date: 2021/6/25 20:40
 */
@Table(name="tb_user_order_map")
@Data
public class UserOrderMap {
    private String username;
    @Column(name = "order_id")
    private String orderId;

}
