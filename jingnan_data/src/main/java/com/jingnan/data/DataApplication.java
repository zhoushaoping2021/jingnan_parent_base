package com.jingnan.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: sublun
 * @Date: 2021/2/8 16:35
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@MapperScan(basePackages = {"com.jingnan.data.mapper","com.jingnan.data.smapper"})
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class);
    }
}
