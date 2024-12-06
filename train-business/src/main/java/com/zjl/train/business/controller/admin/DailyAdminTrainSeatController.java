package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.DailyTrainSeatQueryReq;
import com.zjl.train.business.request.DailyTrainSeatSaveReq;
import com.zjl.train.business.resp.DailyTrainSeatQueryResponse;
import com.zjl.train.business.service.DailyTrainSeatService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 每日车次座位信息信息控制层 - 管理端
 * Created By Zhangjilin 2024/12/2
 */
@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyAdminTrainSeatController {

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> saveSeat(@Valid @RequestBody DailyTrainSeatSaveReq req) {
        dailyTrainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainSeatQueryResponse>> querySeatList(@Valid DailyTrainSeatQueryReq req) {
        PageResp<DailyTrainSeatQueryResponse> trainSeatList = dailyTrainSeatService.queryList(req);
        return new CommonResp<>(trainSeatList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteSeat(@PathVariable Long id){
        dailyTrainSeatService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<DailyTrainSeatQueryResponse>> queryList() {
        List<DailyTrainSeatQueryResponse> list = dailyTrainSeatService.queryAll();
        return new CommonResp<>(list);
    }
}
