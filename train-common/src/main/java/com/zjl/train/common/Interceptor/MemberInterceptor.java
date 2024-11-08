package com.zjl.train.common.Interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zjl.train.common.context.LoginMemberContext;
import com.zjl.train.common.resp.MemberLoginResponse;
import com.zjl.train.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器：用于拦截登录请求，保存用户信息进入ThreadLocal中
 * Created By Zhangjilin 2024/11/08
 */
@Component
public class MemberInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取header的token参数
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            LOG.info("获取会员登录token：{}", token);
            // 解析token，将token变成会员信息
            JSONObject loginMember = JwtUtil.getJSONObject(token);
            LOG.info("当前登录会员：{}", loginMember);
            // 解析成Response
            MemberLoginResponse member = JSONUtil.toBean(loginMember, MemberLoginResponse.class);
            // 存入线程变量中
            LoginMemberContext.setMember(member);
        }
        return true;
    }
}
