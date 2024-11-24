package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.TrainCarriageQueryReq;
import com.zjl.train.business.request.TrainCarriageSaveReq;
import com.zjl.train.business.resp.TrainCarriageQueryResponse;
import com.zjl.train.business.service.TrainCarriageService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车次车厢信息信息控制层 - 管理端
 * Created By Zhangjilin 2024/11/24
 */
@RestController
@RequestMapping("/admin/trainCarriage")
public class AdminTrainCarriageController {

    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody TrainCarriageSaveReq req) {
        trainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainCarriageQueryResponse>> queryPassengerList(@Valid TrainCarriageQueryReq req) {
        PageResp<TrainCarriageQueryResponse> trainCarriageList = trainCarriageService.queryList(req);
        return new CommonResp<>(trainCarriageList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        trainCarriageService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainCarriageQueryResponse>> queryList() {
        List<TrainCarriageQueryResponse> list = trainCarriageService.queryAll();
        return new CommonResp<>(list);
    }
}
