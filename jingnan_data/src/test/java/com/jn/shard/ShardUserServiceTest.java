package com.jn.shard;

import com.jingnan.data.DataApplication;
import com.jingnan.data.service.ShardUserService;
import com.jn.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: sublun
 * @Date: 2021/6/22 22:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataApplication.class)
public class ShardUserServiceTest {
    @Autowired
    private ShardUserService shardUserService;

    @Test
    public void testGetUserList() {
        List<User> userList = shardUserService.getUserList();
        userList.forEach(user -> System.out.println(user));

    }
}
