package com.bishugui.demoqps.config;

import com.bishugui.demoqps.interceptor.QpsInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author bi shugui
 * @description web 配置类
 * @date 2023/2/7 21:16
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    QpsInterceptor qpsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(qpsInterceptor).addPathPatterns("/api/**");
    }
}
