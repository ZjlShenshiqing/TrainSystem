package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.DailyTrainQueryReq;
import com.zjl.train.business.request.DailyTrainSaveReq;
import com.zjl.train.business.resp.DailyTrainQueryResponse;
import com.zjl.train.business.service.DailyTrainService;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 每日车次信息控制层 - 管理端
 * Created By Zhangjilin 2024/11/30
 */
@RestController
@RequestMapping("/admin/daily-train")
public class DailyAdminTrainController {

    @Resource
    private DailyTrainService dailyTrainService;

    @Resource
    private TrainSeatService seatService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody DailyTrainSaveReq req) {
        dailyTrainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainQueryResponse>> queryPassengerList(@Valid DailyTrainQueryReq req) {
        PageResp<DailyTrainQueryResponse> trainList = dailyTrainService.queryList(req);
        return new CommonResp<>(trainList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        dailyTrainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<DailyTrainQueryResponse>> queryList() {
        List<DailyTrainQueryResponse> list = dailyTrainService.queryAll();
        return new CommonResp<>(list);
    }

    /**
     * 自动通过车次生成座位功能
     * Created By Zhangjilin 2024/11/30
     */
    @GetMapping("/auto-seat/{trainCode}")
    public CommonResp<Object> autoSeat(@PathVariable String trainCode) {
        seatService.autoTrainSeat(trainCode);
        return new CommonResp<>();
    }
}
