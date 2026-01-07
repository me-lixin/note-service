package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itlixin.nodeservice.entity.User;
import com.itlixin.nodeservice.mapper.UserMapper;
import com.itlixin.nodeservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserMapper userMapper;

    public String login(String username, String password) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("用户名或密码错误");
        }

        return JwtUtil.generateToken(user.getId());
    }

    public Integer register(User req) {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, req.getUsername()));
        if (users.isEmpty()){
           return userMapper.insert(req);
        }else {
            throw new RuntimeException("用户已经存在!");
        }
    }
}

