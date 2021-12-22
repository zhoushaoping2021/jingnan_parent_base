package com.jn.web.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.jn.web.goods.service.OrderService;
import com.jn.web.goods.service.WxPayService;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付业务
 */
@Controller
@RequestMapping("/wpay")
public class WebPayController {

    @Autowired
    WxPayService wxPayService;

    @Autowired
    OrderService orderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private String userName="sunwukong";
    /**
     * 微信支付二维码
     * @return
     **/
    @GetMapping("/nativePay")
    public String wxPay(Model model, @Param("orderId") String orderId){
        Map map = wxPayService.nativePay(orderId);
        model.addAttribute("orderId",map.get("orderId"));
        model.addAttribute("payMoney",Double.parseDouble(map.get("payMoney").toString()));
        model.addAttribute("code_url",map.get("code_url"));
        return "wxpay";
    }

    /**
     *  接收支付回调
     **/
    @RequestMapping("/notify")
    @ResponseBody
    public Map notify(HttpServletRequest request) throws Exception {
        System.out.println("==================接收到支付回调数据==========================");
        try {
            ServletInputStream inputStream = request.getInputStream();
            String xml = new String(IOUtils.toByteArray(inputStream));
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //1： 更新订单
            orderService.updateOrderPayStatus(resultMap.get("out_trade_no"),resultMap.get("transaction_id"));
            //发消息 2： 通知网页成功
            System.out.println("发消息到页面：" + resultMap.get("out_trade_no"));
            rabbitTemplate.convertAndSend("paynotify","", resultMap.get("out_trade_no"));
            //返回成功信息
            Map map = new HashMap();
            map.put("return_code", "SUCCESS");
            map.put("return_msg", "OK");
            return map;
        }catch (Exception e){
            System.out.println(e);

        }
        return null;
    }

    @GetMapping("/paysucces")
    public String paySuccess(@Param("payMoney")Double payMoney,Model model){
        System.out.println(payMoney);
        model.addAttribute("payMoney",payMoney);
        return "paysuccess";
    }

    
}
