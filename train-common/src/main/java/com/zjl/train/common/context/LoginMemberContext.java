package com.zjl.train.common.context;

import com.zjl.train.common.resp.MemberLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 存储会员信息上下文
 * Created By Zhangjilin 2024/11/08
 */
public class LoginMemberContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    private static ThreadLocal<MemberLoginResponse> member = new ThreadLocal<>();

    private static MemberLoginResponse getMember() {
        return member.get();
    }

    public static void setMember(MemberLoginResponse member) {
        LoginMemberContext.member.set(member);
    }

    public static Long getId() {
        try {
            return getMember().getId();
        } catch (Exception e) {
            LOG.error("获取登录会员信息异常", e);
            throw e;
        }
    }
}
