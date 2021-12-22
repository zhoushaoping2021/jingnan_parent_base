package com.jn.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sublun
 * @Date: 2021/2/8 16:45
 */
@Configuration
public class SysConfig {

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(12, 12,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50));
    }
}
