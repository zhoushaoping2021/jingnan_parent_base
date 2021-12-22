package com.jn.web.order.controller;

import com.jn.entity.Result;
import com.jn.pojo.Address;
import com.jn.pojo.Order;
import com.jn.web.goods.service.AddressService;
import com.jn.web.goods.service.CartService;
import com.jn.web.goods.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 结算和保存订单
 */
@Controller
@RequestMapping("/worder")
public class WebOrderController {

    @Autowired
    CartService cartService;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;

    private String username="sunwukong";

    public static final String URL = "http://web.jn.com:9001";


    @RequestMapping("/ready")
    public String readyOrder(Model model) {
        //获取收货地址列表
        Result<List<Address>> addressList = addressService.list(username);
        Address defaultAddress = null;
        if (addressList.getData() != null) {
            for (Address address : addressList.getData()) {
                if ("1".equals(address.getIsDefault())) {
                    defaultAddress = address; } } }
                    //获取购物车
        Map cartMap = cartService.list(username);
        model.addAttribute("addressList", addressList.getData());
        model.addAttribute("cartMap", cartMap);
        model.addAttribute("defaultAddress", defaultAddress);
        return "order";
    }


    @PostMapping(value = "/submit")
    public String submit(Order order){
        //设置购买用户
        order.setUsername(username);
        Boolean flag = orderService.add(order);
        if(flag){ //去支付
            return "redirect:" + URL + "/wpay/nativePay?orderId=" + order.getId();
        }else{
            //回显订单页面 显示库存不足
            System.out.println(" //回显订单页面 显示库存不足");
            return "fail";
        }
    }

    
}
