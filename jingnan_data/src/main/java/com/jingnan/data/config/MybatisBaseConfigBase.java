package com.jingnan.data.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
//@MapperScan(basePackages = "com.jingnan.data.mapper", sqlSessionTemplateRef = "sqlSessionTemplateBase")
@MapperScan(basePackages = "com.jingnan.data.mapper", sqlSessionFactoryRef = "sqlSessionFactoryBase")
public class MybatisBaseConfigBase {

    /**
     * 连接本地数据库的数据源
     * @return
     */
    @Bean(name="baseDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.base")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 连接本地数据库的SQLSessionFactory
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBase() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource1());

        return factoryBean.getObject();

    }

    /**
     * 连接本地数据库的SqlSessionTemplate
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplateBase() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryBase());
        return template;
    }

}
