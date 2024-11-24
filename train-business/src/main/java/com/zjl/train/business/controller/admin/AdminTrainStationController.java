package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.TrainStationQueryReq;
import com.zjl.train.business.request.TrainStationSaveReq;
import com.zjl.train.business.resp.TrainStationQueryResponse;
import com.zjl.train.business.service.TrainStationService;
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
@RequestMapping("/admin/trainStation")
public class AdminTrainStationController {

    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResponse>> queryPassengerList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResponse> trainStationList = trainStationService.queryList(req);
        return new CommonResp<>(trainStationList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        trainStationService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainStationQueryResponse>> queryList() {
        List<TrainStationQueryResponse> list = trainStationService.queryAll();
        return new CommonResp<>(list);
    }
}
