package com.asphyxia.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebMvcConfigurer implements WebMvcConfigurer {
    @Autowired
    private ProjectInterceptor projectInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //"/brain/**"
        registry.addInterceptor(projectInterceptor).addPathPatterns("/**").excludePathPatterns(
                "/pages/login.html",
                "/pages/register.html",
                "/assets/**",
                "/css/**",
                "/js/**",
                "/plugins/**",
                "/index.html",
                "/user/**"
                );
    }


}
