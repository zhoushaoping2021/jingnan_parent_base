package com.jn.web.goods.controller;

import com.jn.entity.Result;
import com.jn.web.goods.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-01
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    String username="sunwukong";

    /**
     * 查询购物车列表数据, 返回到页面中并展示
     * @return
     */
    @GetMapping("/list")
    public Map<String, Object> list() {
        //获取当前登录用户的用户名
        /*Map<String, String> userInfo
         tokenDecode.getUserInfo();
        String username = userInfo.get("username");*/

        //调用service, 获取购物车列表页面需要的所有数据返回
        Map<String, Object> resultMap = cartService.list(username);
        return resultMap;

    }

    /**
     * 添加修改商品到购物车中
     * @param skuId 商品的库存id
     * @param num   购买数量
     * @return
     */
    @GetMapping("/add/{skuId}/{num}")
    public void addAndupdate(@PathVariable(value = "skuId") String skuId,
                               @PathVariable(value = "num") Integer num) {
        /*//获取当前登录用户的用户名
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");*/


        //添加商品到购物车中
        cartService.addAndUpdate(username, skuId, num);
    }



    /**
     * 根据skuid删除购物车中的购物项数据
     * @param skuId
     * @return
     */
    @GetMapping("/del/{skuId}")
    public void del(@PathVariable(value = "skuId") String skuId) {
        //1. 获取当前登录用户的用户名
       /* Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");*/

        //2. 调用service方法删除
        cartService.del(username, skuId);
    }

    /**
     * 复选框勾选与取消勾选
     * @return
     */
    @GetMapping("/updateChecked/{skuId}/{checked}")
    public void updateChecked(@PathVariable(value = "skuId") String skuId,
                              @PathVariable(value = "checked")  Boolean checked) {


        //获取当前登录用户的用户名
       /* Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");*/

        cartService.updateChecked(username, skuId, checked);
    }

}
