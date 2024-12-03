package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.DailyTrainSeatQueryReq;
import com.zjl.train.business.request.DailyTrainSeatSaveReq;
import com.zjl.train.business.request.TrainSeatQueryReq;
import com.zjl.train.business.request.TrainSeatSaveReq;
import com.zjl.train.business.resp.DailyTrainSeatQueryResponse;

import com.zjl.train.business.resp.TrainSeatQueryResponse;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车次车厢信息信息控制层 - 管理端
 * Created By Zhangjilin 2024/11/24
 */
@RestController
@RequestMapping("/admin/trainSeat")
public class AdminTrainSeatController {

    @Autowired
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainSeatQueryResponse>> queryPassengerList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResponse> trainSeatList = trainSeatService.queryList(req);
        return new CommonResp<>(trainSeatList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        trainSeatService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainSeatQueryResponse>> queryList() {
        List<TrainSeatQueryResponse> list = trainSeatService.queryAll();
        return new CommonResp<>(list);
    }
}
