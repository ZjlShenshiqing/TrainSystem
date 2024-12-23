package com.zjl.train.member.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.util.JwtUtil;
import com.zjl.train.common.util.SnowUtil;
import com.zjl.train.member.config.MemberApplication;
import com.zjl.train.member.entity.Member;
import com.zjl.train.member.entity.MemberExample;
import com.zjl.train.member.mapper.MemberMapper;
import com.zjl.train.member.request.MemberLoginReq;
import com.zjl.train.member.request.MemberRegisterReq;
import com.zjl.train.member.request.MemberSendCodeReq;
import com.zjl.train.member.response.MemberLoginResponse;
import com.zjl.train.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 会员服务层实现类
 * Created By Zhangjilin 2024/10/29
 */
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberApplication.class);

    @Autowired
    private RedisTemplate redisTemplate;

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
        Member memberFromDB = selectByMobile(mobile);

        // 判断list是否为空
        if (ObjectUtil.isNotNull(memberFromDB)) {
            // return list.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    /**
     * 发送验证码实现类 Created By Zhangjilin 2024/11/04
     * @param request 封装完成的请求体
     * @return
     */
    @Override
    public void sendCode(MemberSendCodeReq request) {
        String mobile = request.getMobile();
        // 注册之前，先检查手机号有没有注册过
        Member memberFromDB = selectByMobile(mobile);

        // 判断是否注册过的逻辑，这里没注册过就会进入
        if (ObjectUtil.isNull(memberFromDB)) {
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
        LOG.info("生成短信验证码: " + code);

        // 将验证码写入 Redis，设置5分钟过期时间
        String redisKey = "member:verify_code:" + mobile;
        redisTemplate.opsForValue().set(redisKey, code, 5, TimeUnit.MINUTES);

        // 保存短信记录表：表结构（手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间）

        // 对接短信通道，发送短信
    }

    /**
     * 会员登录实现类 Created By Zhangjilin 2024/11/04
     * @param request 封装完成的请求体
     * @return
     */
    @Override
    public MemberLoginResponse login(MemberLoginReq request) {
        // 获取手机号和验证码
        String mobile = request.getMobile();
        String code = request.getCode();

        // 查询手机号是否存在
        Member memberFromDB = selectByMobile(mobile);

        // 如果手机号不存在，则抛出异常，因为没有获取验证码
        if (ObjectUtil.isNull(memberFromDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        // 从 Redis 中获取验证码
        String redisKey = "member:verify_code:" + mobile;
        String codeFromRedis = (String) redisTemplate.opsForValue().get(redisKey);

        // 校验短信验证码
        // 验证码过期
        if (ObjectUtil.isNull(codeFromRedis)) {
            throw new BusinessException(BusinessExceptionEnum.SMS_CODE_EXPIRED);
        }

        // 验证码错误
        if (!codeFromRedis.equals(code)) {
            throw new BusinessException(BusinessExceptionEnum.INVALID_SMS_CODE);
        }

        // 验证通过，返回用户信息（头像，昵称）
        MemberLoginResponse memberLoginResponse = BeanUtil.copyProperties(memberFromDB, MemberLoginResponse.class);
        // 使用工具类生成token
        String token = JwtUtil.createToken(memberLoginResponse.getId(), memberLoginResponse.getMobile());
        memberLoginResponse.setToken(token);
        return memberLoginResponse;
    }

    /**
     * 根据手机号查询会员
     * Created By Zhangjilin 2024/11/04
     * @param mobile
     * @return
     */
    private Member selectByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
