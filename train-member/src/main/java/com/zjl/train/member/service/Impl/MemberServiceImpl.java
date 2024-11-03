package com.zjl.train.member.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.member.entity.Member;
import com.zjl.train.member.entity.MemberExample;
import com.zjl.train.member.mapper.MemberMapper;
import com.zjl.train.member.req.MemberRegisterReq;
import com.zjl.train.member.service.MemberService;
import com.zjl.train.member.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务层实现类
 * Created By Zhangjilin 2024/10/29
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    /**
     * 会员注册实现类 Created By Zhangjilin 2024/10/29
     * @param request 封装完成的请求体
     * @return
     */
    @Override
    public Long register(MemberRegisterReq request) {
        String mobile = request.getMobile();
        // 注册之前，先检查手机号有没有注册过
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        // 判断list是否为空
        if (CollUtil.isNotEmpty(list)) {
            // return list.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
