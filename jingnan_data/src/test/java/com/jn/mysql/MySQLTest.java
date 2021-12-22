package com.jn.mysql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author: sublun
 * @Date: 2021/5/14 13:47
 */
public class MySQLTest {

    /**
     * ShardingProxy测试
     * @throws Exception
     */
    @Test
    public void testShardingProxy() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.68.140:3306/hello?serverTimezone=GMT%2B8&useSSL=false", "admin", "admin");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from t1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }

    @Test
    public void testShardingProxy2() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.68.140:3306/hello?serverTimezone=GMT%2B8&useSSL=false", "admin", "admin");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from t1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }

    @Test
    public void testHashCode() {
        System.out.println("admin".hashCode());
        for (int i = 0; i < 10; i++) {
            int hashCode = ("admin" + i).hashCode();
            System.out.println(hashCode);
            System.out.println(Math.abs(hashCode)%2);
        }
    }

    @Test
    public void testPxcCluster() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.68.145:3306/sb_test?serverTimezone=GMT%2B8&useSSL=false", "admin", "admin");

        PreparedStatement preparedStatement = connection.prepareStatement("select @@hostname");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }
}
