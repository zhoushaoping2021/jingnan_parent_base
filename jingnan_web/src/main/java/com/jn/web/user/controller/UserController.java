package com.jn.web.user.controller;

import com.jn.entity.Result;
import com.jn.entity.StatusCode;
import com.jn.pojo.User;
import com.jn.web.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Author yaxiongliu
 **/
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    //根据id查询用户信息
    @RequestMapping("/findByUsername/{username}")
    public Result<User> findByUsername(@PathVariable("username") String username){
        User user = userService.findByUsername(username);
        Result<User> result = new Result<>();
        result.setData(user);
        result.setCode(StatusCode.OK);
        result.setMessage("查询成功");
        return result;
    }
}
