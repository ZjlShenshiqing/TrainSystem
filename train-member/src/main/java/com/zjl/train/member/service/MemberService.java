package com.zjl.train.member.service;

import com.zjl.train.member.req.MemberLoginReq;
import com.zjl.train.member.req.MemberRegisterReq;
import com.zjl.train.member.req.MemberSendCodeReq;
import com.zjl.train.member.response.MemberLoginResponse;
import org.springframework.stereotype.Service;

/**
 * 服务层接口
 * Created By Zhangjilin 2024/10/29
 */
@Service
public interface MemberService {
    int count();

    /**
     * Created By Zhangjilin 2024/10/29
     * 会员注册方法
     */
    Long register(MemberRegisterReq request);

    /**
     * Create By Zhangjilin 2024/11/04
     * 发送短信验证码方法
     */
    void sendCode(MemberSendCodeReq request);

    /**
     * Create By Zhangjilin 2024/11/04
     * 登录方法
     */
    MemberLoginResponse login(MemberLoginReq request);
}
