package com.zjl.train.member.controller;


import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.member.req.MemberLoginReq;
import com.zjl.train.member.req.MemberRegisterReq;
import com.zjl.train.member.req.MemberSendCodeReq;
import com.zjl.train.member.response.MemberLoginResponse;
import com.zjl.train.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 注册
     * Create By Zhangjilin 2024/10/29
     * @param request
     * @return
     */
    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq request) {
        Long register = memberService.register(request);
        return new CommonResp<>(register);
    }

    /**
     * 发送短信验证码
     * Create By Zhangjilin 2024/11/04
     * @param request
     * @return
     */
    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid MemberSendCodeReq request) {
        memberService.sendCode(request);
        return new CommonResp<>();
    }

    /**
     * 登录
     * Create By Zhangjilin 2024/11/04
     * @param request
     * @return
     */
    @PostMapping("/login")
    public CommonResp<MemberLoginResponse> sendCode(@Valid MemberLoginReq request) {
        MemberLoginResponse login = memberService.login(request);
        return new CommonResp<>(login);
    }
}
