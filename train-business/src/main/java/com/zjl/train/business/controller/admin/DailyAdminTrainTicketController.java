package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.DailyTrainTicketQueryReq;
import com.zjl.train.business.request.DailyTrainTicketSaveReq;
import com.zjl.train.business.resp.DailyTrainTicketQueryResponse;
import com.zjl.train.business.service.DailyTrainTicketService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 每日余票信息控制层 - 管理端
 * Created By Zhangjilin 2024/12/4
 */
@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyAdminTrainTicketController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;


    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainTicketSaveReq req) {
        dailyTrainTicketService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResponse>> queryList(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResponse> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainTicketService.delete(id);
        return new CommonResp<>();
    }
}
