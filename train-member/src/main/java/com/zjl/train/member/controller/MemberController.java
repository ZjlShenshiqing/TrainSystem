package com.zjl.train.member.controller;


import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.member.req.MemberRegisterReq;
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
    public CommonResp<Integer> count() {
        int count = memberService.count();
        CommonResp<Integer> commonResp = new CommonResp<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(MemberRegisterReq request) {
        Long register = memberService.register(request);
        return new CommonResp<>(register);
    }
}
