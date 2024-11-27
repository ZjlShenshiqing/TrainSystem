package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.TrainQueryReq;
import com.zjl.train.business.request.TrainSaveReq;
import com.zjl.train.business.resp.TrainQueryResponse;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.business.service.TrainService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车次信息控制层 - 管理端
 * Created By Zhangjilin 2024/11/22
 */
@RestController
@RequestMapping("/admin/train")
public class AdminTrainController {

    @Resource
    private TrainService trainService;

    @Resource
    private TrainSeatService seatService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResponse>> queryPassengerList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResponse> trainList = trainService.queryList(req);
        return new CommonResp<>(trainList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        trainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResponse>> queryList() {
        List<TrainQueryResponse> list = trainService.queryAll();
        return new CommonResp<>(list);
    }

    /**
     * 自动通过车次生成座位功能
     * Created By Zhangjilin 2024/11/27
     */
    @GetMapping("/auto-seat/{trainCode}")
    public CommonResp<Object> autoSeat(@PathVariable String trainCode) {
        seatService.autoTrainSeat(trainCode);
        return new CommonResp<>();
    }
}
