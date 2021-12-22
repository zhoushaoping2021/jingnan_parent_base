package com.jingnan.data.service;

import com.github.pagehelper.PageHelper;
import com.jingnan.data.config.ShardingDataSource;
import com.jingnan.data.mapper.UserMapper;
import com.jn.entity.Result;
import com.jn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Result importUser() throws SQLException {
        PageHelper.startPage(1,10);
        //查询本地数据库中的用户列表
        List<User> userList = userMapper.selectAll();
        System.out.println(userList.size());
        //获得集群数据库数据源
        DataSource dataSource = ShardingDataSource.getInstance();
        Connection connection = dataSource.getConnection();
        //将用户信息写入数据库集群
        userList.stream().forEach(u->insertShardingUser(connection, u));
        //插入完成，回收连接
        connection.close();
        //返回结果
        return Result.builder()
                .code(200)
                .flag(true)
                .message("数据导入完成")
                .build();
    }

    private void insertShardingUser(Connection conn, User user) {
        String sql = "insert into tb_user(\n" +
                "  username,\n" +
                "  password,\n" +
                "  phone,\n" +
                "  email,\n" +
                "  created,\n" +
                "  updated,\n" +
                "  source_type,\n" +
                "  nick_name,\n" +
                "  name,\n" +
                "  status,\n" +
                "  head_pic,\n" +
                "  qq,\n" +
                "  is_mobile_check,\n" +
                "  is_email_check,\n" +
                "  sex,\n" +
                "  user_level,\n" +
                "  points,\n" +
                "  experience_value,\n" +
                "  birthday,\n" +
                "  last_login_time\n" +
                ")\n" +
                "values(\n" +
                "  ?,?,?,?,?,\n" +
                "  ?,?,?,?,?,\n" +
                "  ?,?,?,?,?,\n" +
                "  ?,?,?,?,?\n" +
                ")";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getEmail());
            if (user.getCreated() != null) {
                preparedStatement.setDate(5, new Date(user.getCreated().getTime()));
            } else {
                preparedStatement.setDate(5, null);
            }
            if (user.getUpdated() != null) {
                preparedStatement.setDate(6, new Date(user.getUpdated().getTime()));
            } else {
                preparedStatement.setDate(6, null);
            }
            preparedStatement.setString(7, user.getSourceType());
            preparedStatement.setString(8, user.getNickName());
            preparedStatement.setString(9, user.getName());
            preparedStatement.setString(10, user.getStatus());
            preparedStatement.setString(11, user.getHeadPic());
            preparedStatement.setString(12, user.getQq());
            preparedStatement.setString(13, user.getIsMobileCheck());
            preparedStatement.setString(14, user.getIsEmailCheck());
            preparedStatement.setString(15, user.getSex());
            if (user.getUserLevel() != null) {
                preparedStatement.setInt(16, user.getUserLevel());
            } else {
                preparedStatement.setNull(16, Types.INTEGER);
            }
            if (user.getPoints() != null) {
                preparedStatement.setInt(17, user.getPoints());
            } else {
                preparedStatement.setNull(17, Types.INTEGER);
            }
            if (user.getExperienceValue() != null) {
                preparedStatement.setInt(18, user.getExperienceValue());
            } else {
                preparedStatement.setNull(18, Types.INTEGER);
            }
            if (user.getBirthday() != null) {
                preparedStatement.setDate(19, new Date(user.getBirthday().getTime()));
            } else {
                preparedStatement.setNull(19, Types.DATE);
            }
            if (user.getLastLoginTime() != null) {
                preparedStatement.setDate(20, new Date(user.getLastLoginTime().getTime()));
            } else {
                preparedStatement.setNull(20, Types.DATE);
            }

            //执行插入操作
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void deleteUsers() throws Exception {
        DataSource dataSource = ShardingDataSource.getInstance();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement("delete from tb_user");
        statement.execute();
        connection.commit();
    }
}