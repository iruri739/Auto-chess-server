package com.accenture.huaweigroup.config;

import interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**/**")
                .excludePathPatterns("/user/login", "/user/register",
                        "/swagger-ui.html", "/webjars/springfox-swagger-ui/**",
                        "//swagger-resources/**");
    }
}
