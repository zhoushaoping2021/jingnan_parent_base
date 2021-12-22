package com.jn.web.user.service;


import com.jn.pojo.User;

public interface UserService {
    /**
     * 根据用户名查询用户信息
     */
    User findByUsername(String username);
}
