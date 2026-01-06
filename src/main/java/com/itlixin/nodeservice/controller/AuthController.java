package com.itlixin.nodeservice.controller;

import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.User;
import com.itlixin.nodeservice.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody User req) {
        return Result.ok(authService.login(req.getUsername(), req.getPassword()));
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User req) {
        authService.register(req);
        return Result.ok();
    }
}

