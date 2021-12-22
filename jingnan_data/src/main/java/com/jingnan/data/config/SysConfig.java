package com.jingnan.data.config;

import com.jn.util.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author: sublun
 * @Date: 2021/2/8 16:45
 */
@Configuration
public class SysConfig {
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(12, 12,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
    }
}
