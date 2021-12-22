package com.jingnan.data.service;

import com.jingnan.data.mapper.UserMapper;
import com.jingnan.data.smapper.SUserMapper;
import com.jn.entity.Result;
import com.jn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.SQLException;
import java.util.List;


/**
 * @Author: sublun
 * 将本地数据库中的用户信息导入到分片数据库中
 */
@Service
public class UserService2 {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SUserMapper sUserMapper;

    public Result importUser() throws SQLException {
        //查询本地数据库中的用户列表
        List<User> userList = userMapper.selectAll();

        //将用户信息写入数据库集群
        userList.stream().forEach(u->sUserMapper.insert(u));
        //返回结果
        return Result.builder()
                .code(200)
                .flag(true)
                .message("数据导入完成")
                .build();
    }

    /**
     * 删除分片数据库中的所有用户
     * @return
     */
    public Result deleteAllUsers() {
        sUserMapper.deleteByExample(new Example(User.class));
        return Result.builder().flag(true).build();
    }


}
