package com.zjl.train.member.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.zjl.train.member.entity.Member;
import com.zjl.train.member.entity.MemberExample;
import com.zjl.train.member.mapper.MemberMapper;
import com.zjl.train.member.service.MemberService;
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
     * @param mobile 手机号
     * @return
     */
    @Override
    public Long register(String mobile) {
        // 注册之前，先检查手机号有没有注册过
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        // 判断list是否为空
        if (CollUtil.isNotEmpty(list)) {
            // return list.get(0).getId();
            throw new RuntimeException("手机号已被注册！");
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
