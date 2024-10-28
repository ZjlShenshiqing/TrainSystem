package com.zjl.train.member.service.Impl;

import com.zjl.train.member.mapper.MemberMapper;
import com.zjl.train.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public int count() {
        return memberMapper.count();
    }
}
