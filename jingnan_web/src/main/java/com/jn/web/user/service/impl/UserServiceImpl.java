package com.jn.web.user.service.impl;

import com.jn.pojo.User;
import com.jn.web.user.dao.UserMapper;
import com.jn.web.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/*
 * @Author yaxiongliu
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        //主键查询,CPU不耗时操作
        User user = userMapper.selectByPrimaryKey(username);

//        try {
//            TimeUnit.SECONDS.sleep(1);
//            log.info("模拟耗时操作，睡眠1s时间");
//            log.info("对象占用JVM堆内存大小：{}"+ RamUsageEstimator.sizeOfObject(user));
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return user;
    }
}
