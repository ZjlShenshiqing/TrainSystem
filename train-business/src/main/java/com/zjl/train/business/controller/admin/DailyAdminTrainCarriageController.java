package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.DailyTrainCarriageQueryReq;
import com.zjl.train.business.request.DailyTrainCarriageSaveReq;
import com.zjl.train.business.resp.DailyTrainCarriageQueryResponse;
import com.zjl.train.business.service.DailyTrainCarriageService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 每日车次车厢信息信息控制层 - 管理端
 * Created By Zhangjilin 2024/12/2
 */
@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyAdminTrainCarriageController {

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> saveCarriage(@Valid @RequestBody DailyTrainCarriageSaveReq req) {
        dailyTrainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainCarriageQueryResponse>> queryCarriageList(@Valid DailyTrainCarriageQueryReq req) {
        PageResp<DailyTrainCarriageQueryResponse> trainCarriageList = dailyTrainCarriageService.queryList(req);
        return new CommonResp<>(trainCarriageList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteCarriage(@PathVariable Long id){
        dailyTrainCarriageService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<DailyTrainCarriageQueryResponse>> queryList() {
        List<DailyTrainCarriageQueryResponse> list = dailyTrainCarriageService.queryAll();
        return new CommonResp<>(list);
    }
}
