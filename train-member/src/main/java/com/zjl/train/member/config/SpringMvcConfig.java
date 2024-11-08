package com.zjl.train.member.config;


import com.zjl.train.common.Interceptor.LogInterceptor;
import com.zjl.train.common.Interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册自定义的拦截器
 * Created By Zhangjilin 2024/11/08
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Resource
    MemberInterceptor memberInterceptor;

    @Resource
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(logInterceptor).addPathPatterns("/**");

        registry.addInterceptor(memberInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                    "/member/member/send-code",
                    "member/member/login"
            );
    }
}
