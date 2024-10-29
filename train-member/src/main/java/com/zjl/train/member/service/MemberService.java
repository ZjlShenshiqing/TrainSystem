package com.zjl.train.member.service;

import com.zjl.train.member.req.MemberRegisterReq;
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
}
