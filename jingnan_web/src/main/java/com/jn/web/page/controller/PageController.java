package com.jn.web.page.controller;

import com.jn.entity.Result;
import com.jn.entity.StatusCode;
import com.jn.web.goods.service.CartService;
import com.jn.web.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 商品详情页测试controller
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private CartService cartService;

    String username="sunwukong";

    /**
     *
     * @param spuId
     * @return
     * @throws Exception
     */
    @GetMapping("/createHtml/{spuId}")
    public Result messageHandler(@PathVariable("spuId") String spuId) throws Exception {
        //1. 根据spuid获取商品详情页需要的所有数据
        Map<String, Object> dataMap = pageService.findPageAllDataBySpuId(spuId);
        //2. 根据上架商品的对应数据和模板生成商品详情页面
        pageService.createStaticPage(dataMap, spuId);

        return Result.builder().code(StatusCode.OK).message("success").build();
    }

    /**
     * 添加修改商品到购物车中
     * @param skuId 商品的库存id
     * @param num   购买数量
     * @return
     */
    @GetMapping("/addCart/{skuId}/{num}")
    public String addCartReturnPage(@PathVariable(value = "skuId") String skuId,
                                    @PathVariable(value = "num") Integer num, Model model) {
        /*//获取当前登录用户的用户名
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");*/


        //添加商品到购物车中
        cartService.addAndUpdate(username, skuId, num);
        return "success-cart";
    }
}
