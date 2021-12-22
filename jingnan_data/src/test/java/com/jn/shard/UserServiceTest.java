package com.jn.shard;

import com.jingnan.data.DataApplication;
import com.jingnan.data.service.DataService;
import com.jingnan.data.service.UserService;
import com.jingnan.data.service.UserService2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

/**
 * @Author: sublun
 * @Date: 2021/6/22 22:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserService2 userService2;
    @Autowired
    private DataService dataService;

    @Test
    public void testImportUser() throws SQLException {
        userService.importUser();
    }

    @Test
    public void testDeleteUser() throws Exception {
        userService.deleteUsers();
    }

    @Test
    public void testDeleteUser2() {
        userService2.deleteAllUsers();
    }

    @Test
    public void testImportUsers2() throws SQLException {
        userService2.importUser();
    }

    @Test
    public void testShardData() throws SQLException {
        dataService.importAllData();
    }



}
