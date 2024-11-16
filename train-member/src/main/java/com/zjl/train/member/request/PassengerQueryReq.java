package com.zjl.train.member.request;

import com.zjl.train.common.request.PageRequest;

/**
 * 新增，保存 封装请求参数的DTO
 * Created By Zhangjilin 2024/11/08
 */
public class PassengerQueryReq extends PageRequest {

    private Long memberId;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return "PassengerQueryReq{" +
                "memberId=" + memberId +
                '}';
    }
}