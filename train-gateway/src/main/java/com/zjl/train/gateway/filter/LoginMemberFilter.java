package com.zjl.train.gateway.filter;

import com.zjl.train.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用于拦截请求，并校验用户的token
 * Created By Zhangjilin 2024/11/07
 */
@Component
public class LoginMemberFilter implements Ordered, GlobalFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 排除不需要拦截的请求
        if (path.matches(".*/admin/.*")
                || path.equals("/hello")
                || path.matches(".*/member/.*/login")
                || path.matches(".*/member/.*/send-code")
                || path.matches("/admin")) {
            LOG.info("不需要登录验证：{}", path);
            return chain.filter(exchange);
        }

        LOG.info("需要登录验证");

        // 获取header的token参数
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("会员登录验证开始，token:{}", token);

        if (token == null || token.isEmpty()) {
            LOG.info("token为空，拦截请求！");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 校验token是否有效，包括token是否被改过，是否过期
        boolean validate = JwtUtil.validate(token);
        if (validate) {
            LOG.info("token有效，放行该请求");
            // 返回过滤器链继续处理请求
            return chain.filter(exchange);
        } else {
            LOG.warn("token无效，请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    /**
     * 优先级设置，越小，优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
