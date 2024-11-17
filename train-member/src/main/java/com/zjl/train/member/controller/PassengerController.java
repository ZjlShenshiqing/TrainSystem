package com.zjl.train.member.controller;

import com.zjl.train.common.context.LoginMemberContext;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.member.request.PassengerQueryReq;
import com.zjl.train.member.request.PassengerSaveReq;
import com.zjl.train.member.response.PassengerQueryResponse;
import com.zjl.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 乘车人模块控制层
 * Created By Zhangjilin 2024/11/08
 */
@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResponse>> queryPassengerList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResponse> passergerList = passengerService.queryList(req);
        return new CommonResp<>(passergerList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        passengerService.delete(id);
        return new CommonResp<>();
    }
}
