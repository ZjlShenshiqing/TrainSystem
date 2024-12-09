package com.zjl.train.business.controller.admin;

import com.zjl.train.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
