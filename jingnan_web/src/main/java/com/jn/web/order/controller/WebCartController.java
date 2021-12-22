package com.jn.web.order.controller;

import com.jn.web.goods.service.CartService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 购物车controller
 */
@Controller
@RequestMapping("/wcart")
public class WebCartController {

    @Autowired
    CartService cartService;

    String username="sunwukong";

    //未对接网关以及oauth的访问地址
    public static final String URL = "http://web.jn.com:9001";

    /*** * 查询用户购物车列表 * @return */
    @GetMapping(value = "/list")
    public String list(Model model){
        model.addAttribute("result",cartService.list(username));
        model.addAttribute("user",username);
        return "cart";
    }

    //加入购物车
    @GetMapping("/add")
    public String add(String skuId, Integer num){
        cartService.addAndUpdate(username,skuId,num);
        //重定向到 协议://域名:端口/网关中配置的前缀/当前controller地址/查询购物车列 表方法地址
        return "redirect:"+URL+"/wcart/list";
    }


    //删除购物车中商品
    @GetMapping("/del")
    public String delete(@Param("skuId") String skuId){
        cartService.del(username,skuId);
        return "redirect:"+URL+"/wcart/list"; }

}
