package com.jingnan.data.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * @Author: sublun
 */
@Configuration
//@MapperScan(basePackages = "com.jingnan.data.smapper", sqlSessionTemplateRef = "sqlSessionTemplateShard")
@MapperScan(basePackages = "com.jingnan.data.smapper", sqlSessionFactoryRef = "sqlSessionFactoryShard")
public class MybatisBaseConfigShard {

    /**
     * ShardingJDBC的数据源
     * @return
     */
    @Bean(name = "shardDataSource")
    public DataSource dataSource2() {
        return ShardingDataSource.getInstance();
    }

    /**
     * ShardingJDBC的SQLSessionFactory
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryShard() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource2());

        return factoryBean.getObject();

    }


    /**
     * ShardingJDBC的SqlSessionTemplate
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplateShard() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryShard());
        return template;
    }

}
