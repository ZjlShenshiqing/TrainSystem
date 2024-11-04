package com.zjl.train.member.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.member.config.MemberApplication;
import com.zjl.train.member.entity.Member;
import com.zjl.train.member.entity.MemberExample;
import com.zjl.train.member.mapper.MemberMapper;
import com.zjl.train.member.req.MemberRegisterReq;
import com.zjl.train.member.req.MemberSendCodeReq;
import com.zjl.train.member.service.MemberService;
import com.zjl.train.member.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务层实现类
 * Created By Zhangjilin 2024/10/29
 */
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberApplication.class);

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

    @Override
    public void sendCode(MemberSendCodeReq request) {
        String mobile = request.getMobile();
        // 注册之前，先检查手机号有没有注册过
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        // 判断是否注册过的逻辑，这里没注册过就会进入
        if (CollUtil.isEmpty(list)) {
            // 手机号不存在，插入一条记录
            LOG.info("手机号不存在，则插入一条记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            LOG.info("手机号存在，不插入记录");
        }

        // 生成验证码（生成随机字符串）
        String code = RandomUtil.randomString(4);
        LOG.info("生成短信验证码: {}" + code);
        // 保存短信记录表：表结构（手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间）

        // 对接短信通道，发送短信
    }
}
