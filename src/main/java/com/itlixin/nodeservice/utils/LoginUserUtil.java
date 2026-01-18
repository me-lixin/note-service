package com.itlixin.nodeservice.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @description: 当前登陆用户工具类
 * @author: LiXin
 * @create: 2026-01-10 17:21
 **/
public class LoginUserUtil {
    public static Long getUserId() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new RuntimeException("无法获取请求上下文");
        }
        Object userId = attrs.getRequest().getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        return (Long) userId;
    }
}
