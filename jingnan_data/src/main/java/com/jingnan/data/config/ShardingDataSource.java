package com.jingnan.data.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ShardingDataSource {
    private volatile static DataSource dataSource;

    public static DataSource getInstance() {
        if (dataSource == null) {
            synchronized (ShardingDataSource.class) {
                if (dataSource == null) {
                    try {
                        //return create();
                        return createByYaml();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return dataSource;
    }

    private static DataSource create() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第 1 个数据源
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setJdbcUrl("jdbc:mysql://192.168.68.144:3306/jingnan_all?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        dataSource1.setUsername("admin");
        dataSource1.setPassword("admin");
        dataSourceMap.put("ds0", dataSource1);

        // 配置第 2 个数据源
        HikariDataSource dataSource2 = new HikariDataSource();
        dataSource2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource2.setJdbcUrl("jdbc:mysql://192.168.68.145:3306/jingnan_all?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        dataSource2.setUsername("admin");
        dataSource2.setPassword("admin");
        dataSourceMap.put("ds1", dataSource2);

        // 配置 t_order 表规则
        TableRuleConfiguration orderTableRule = new TableRuleConfiguration("tb_order","ds${0..1}.tb_order");
        orderTableRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds${id.toLong() % 2}"));
        // 配置 tb_order_item 表规则
        TableRuleConfiguration orderItemTableRule = new TableRuleConfiguration("tb_order_item","ds${0..1}.tb_order_item");
        orderItemTableRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "ds${order_id.toLong() % 2}"));
        // 配置 t_user 表规则
        TableRuleConfiguration userTableRule = new TableRuleConfiguration("tb_user","ds${0..1}.tb_user");
        userTableRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("username", "ds${Math.abs(username.hashCode()) % 2}"));
        // 配置 tb_user_order_map 表规则
        TableRuleConfiguration userOrderMapTableRule = new TableRuleConfiguration("tb_user_order_map","ds${0..1}.tb_user_order_map");
        userOrderMapTableRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("username", "ds${Math.abs(username.hashCode()) % 2}"));


        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRule);
        shardingRuleConfig.getTableRuleConfigs().add(orderItemTableRule);
        shardingRuleConfig.getTableRuleConfigs().add(userTableRule);
        shardingRuleConfig.getTableRuleConfigs().add(userOrderMapTableRule);

        // 创建数据源
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
        return dataSource;
    }

    private static DataSource createByYaml() throws Exception {
        ClassPathResource yamlRes = new ClassPathResource("data-source.yml");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream inputStream = yamlRes.getInputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while((len = inputStream.read(b)) != -1){
            bos.write(b, 0, len);
        }
        inputStream.close();
        bos.close();
        DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(bos.toByteArray());

        return dataSource;
    }
}
