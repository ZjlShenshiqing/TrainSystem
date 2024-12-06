package com.zjl.train.business.controller.admin;


import com.zjl.train.business.request.DailyTrainStationQueryReq;
import com.zjl.train.business.request.DailyTrainStationSaveReq;
import com.zjl.train.business.resp.DailyTrainStationQueryResponse;
import com.zjl.train.business.service.DailyTrainStationService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车次经停站信息信息控制层 - 管理端
 * Created By Zhangjilin 2024/11/23
 */
@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyAdminTrainStationController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResp<Object> saveTrainStation(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainStationQueryResponse>> queryTrainStationList(@Valid DailyTrainStationQueryReq req) {
        PageResp<DailyTrainStationQueryResponse> DailyTrainStationList = dailyTrainStationService.queryList(req);
        return new CommonResp<>(DailyTrainStationList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteTrainStation(@PathVariable Long id){
        dailyTrainStationService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<DailyTrainStationQueryResponse>> queryList() {
        List<DailyTrainStationQueryResponse> list = dailyTrainStationService.queryAll();
        return new CommonResp<>(list);
    }
}
