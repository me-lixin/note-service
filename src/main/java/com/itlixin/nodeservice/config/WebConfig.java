package com.itlixin.nodeservice.config;

import com.itlixin.nodeservice.utils.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web Api访问配置类
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**","/api/note/share/public/**","/img/**");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 获取 jar 所在目录（本地运行 / jar 运行 都适用）
        String rootPath = System.getProperty("user.dir");

        // 2. 设定保存目录（如：oldImg）
        String saveDirPath = rootPath + File.separator + "img";
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:"+saveDirPath+"/");
    }
}

