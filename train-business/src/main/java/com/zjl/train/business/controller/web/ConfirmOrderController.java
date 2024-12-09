package com.zjl.train.business.controller.web;

import com.zjl.train.business.entity.ConfirmOrder;
import com.zjl.train.business.request.ConfirmOrderDoReq;
import com.zjl.train.business.service.ConfirmOrderService;
import com.zjl.train.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端确认订单，转发后端接口
 * Created By Zhangjilin 2024/12/9
 */
@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Autowired
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public CommonResp<Object> doConfirmOrder(@Valid @RequestBody ConfirmOrderDoReq confirmOrder) {
        confirmOrderService.doConfirm(confirmOrder);
        return new CommonResp<>();
    }
}
