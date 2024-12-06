package com.zjl.train.business.controller.web;

import com.zjl.train.business.request.DailyTrainTicketQueryReq;
import com.zjl.train.business.resp.DailyTrainTicketQueryResponse;
import com.zjl.train.business.service.DailyTrainTicketService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 每日余票信息控制层 - 客户端
 * Created By Zhangjilin 2024/12/6
 */
@RestController
@RequestMapping("/daily-train-ticket")
public class DailyTrainTicketController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResponse>> queryList(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResponse> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }
}
