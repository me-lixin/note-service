package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itlixin.nodeservice.dto.rqs.UserRequest;
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

    public User login(String username, String password) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null) {
            throw new RuntimeException("用户名不存在,请先注册");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("用户名或密码错误");
        }
        user.setToken(JwtUtil.generateToken(user.getId()));
        return user;
    }

    public Integer register(UserRequest req) {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, req.getUsername()));
        if (users.isEmpty()){
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(req.getPassword());
            user.setNickname(req.getNickname());
            return userMapper.insert(user);
        }else {
            throw new RuntimeException("用户已经存在!");
        }
    }
}

