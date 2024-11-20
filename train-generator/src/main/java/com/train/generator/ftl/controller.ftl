package com.zjl.train.member.controller;

import com.zjl.train.common.context.LoginMemberContext;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.member.request.${Domain}QueryReq;
import com.zjl.train.member.request.${Domain}SaveReq;
import com.zjl.train.member.response.${Domain}QueryResponse;
import com.zjl.train.member.service.${Domain}Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 乘车人模块控制层
 * Created By Zhangjilin 2024/11/08
 */
@RestController
@RequestMapping("/${domain}")
public class ${Domain}Controller {

    @Autowired
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResp<Object> save${Domain}(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<${Domain}QueryResponse>> query${Domain}List(@Valid ${Domain}QueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<${Domain}QueryResponse> passergerList = ${domain}Service.queryList(req);
        return new CommonResp<>(passergerList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete${Domain}(@PathVariable Long id){
        ${domain}Service.delete(id);
        return new CommonResp<>();
    }
}