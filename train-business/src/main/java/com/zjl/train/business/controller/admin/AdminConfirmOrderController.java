package com.zjl.train.business.controller.admin;

import com.zjl.train.business.entity.ConfirmOrder;
import com.zjl.train.business.request.ConfirmOrderDoReq;
import com.zjl.train.business.request.ConfirmOrderQueryReq;
import com.zjl.train.business.resp.ConfirmOrderQueryResponse;
import com.zjl.train.business.service.ConfirmOrderService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 确认订单信息 - 管理端控制层
 *
 * Created By Zhangjilin 2024/12/9
 */
@RestController
@RequestMapping("/admin/confirm-order")
public class AdminConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody ConfirmOrderDoReq confirmOrder) {
        confirmOrderService.save(confirmOrder);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<ConfirmOrderQueryResponse>> queryList(@Valid ConfirmOrderQueryReq confirmOrderQueryReq) {
        PageResp<ConfirmOrderQueryResponse> list = confirmOrderService.queryList(confirmOrderQueryReq);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@PathVariable Long id) {
        confirmOrderService.delete(id);
        return new CommonResp<>();
    }
}
