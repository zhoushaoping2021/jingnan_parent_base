package com.jingnan.data.service;

import com.jingnan.data.smapper.SUserMapper;
import com.jn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: sublun
 * @Date: 2021/6/22 22:51
 */
@Service
public class ShardUserService {
    @Autowired
    private SUserMapper sUserMapper;

    public User getUserByName(String username) {
        User user = sUserMapper.selectByPrimaryKey(username);
        return user;
    }

    public List<User> getUserList() {
        return sUserMapper.selectAll();
    }
}
