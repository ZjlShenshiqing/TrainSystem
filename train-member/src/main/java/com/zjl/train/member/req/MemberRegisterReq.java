package com.zjl.train.member.req;

import jakarta.validation.constraints.NotBlank;

/**
 * 封装注册请求参数的DTO
 * Created By Zhangjilin 2024/10/29
 */
public class MemberRegisterReq {

    @NotBlank(message = "[手机号] 不能为空！")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberRegisterReq{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
