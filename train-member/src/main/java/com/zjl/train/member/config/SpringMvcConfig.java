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
    private MemberInterceptor memberInterceptor;

    @Resource
    private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 先注册 MemberInterceptor，再注册 LogInterceptor
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/member/member/send-code",
                        "/member/member/login",
                        "/member/**/send-code",
                        "/member/**/login"
                );

        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }
}
