package com.jn.mysql;

import com.jingnan.data.DataApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @Author: sublun
 * @Date: 2021/6/11 15:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataApplication.class)
public class MysqlTest2 {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ExecutorService executorService;
    @Test
    public void testHostName() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(()->{
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("select @@hostname");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while(resultSet.next()) {
                        System.out.println(connection + "\t" + resultSet.getString(1));
                    }
                    resultSet.close();
                    preparedStatement.close();
                    //connection.close();
                    countDownLatch.countDown();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        }
        countDownLatch.await();
        System.out.println("执行完毕。。。");

    }
}
