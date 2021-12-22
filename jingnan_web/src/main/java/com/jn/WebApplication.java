package com.jn;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WeChatPayConfig;
import com.jn.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.jn.web")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class,args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public WXPay wxPay(){
        try {
            return new WXPay(new WeChatPayConfig() );
        } catch (Exception e) {
            e.printStackTrace();
            return null; }
    }
}
