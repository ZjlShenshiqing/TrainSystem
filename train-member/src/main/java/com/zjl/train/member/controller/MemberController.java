package com.zjl.train.member.controller;


import com.zjl.train.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员模块控制层
 * Created By Zhangjilin 2024/10/29
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public Integer count() {
        return memberService.count();
    }

    @PostMapping("/register")
    public Long register(String mobile) {
        return memberService.register(mobile);
    }
}
