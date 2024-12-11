package com.zjl.train.business.config;


import com.zjl.train.common.Interceptor.LogInterceptor;
import com.zjl.train.common.Interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册自定义的拦截器
 * Created By Zhangjilin 2024/12/2
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    private LogInterceptor logInterceptor;

    @Resource
    private MemberInterceptor memberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 先注册 MemberInterceptor，再注册 LogInterceptor
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }
}
