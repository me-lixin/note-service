package com.itlixin.nodeservice.config;

import com.itlixin.nodeservice.dto.resp.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handle(RuntimeException e) {
        return Result.fail(e.getMessage());
    }
}
