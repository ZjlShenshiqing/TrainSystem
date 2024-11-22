package com.zjl.train.business.controller.admin;

import com.zjl.train.business.request.StationQueryReq;
import com.zjl.train.business.request.StationSaveReq;
import com.zjl.train.business.resp.StationQueryResponse;
import com.zjl.train.business.service.StationService;
import com.zjl.train.common.resp.CommonResp;
import com.zjl.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车站信息控制层 - 管理端
 * Created By Zhangjilin 2024/11/20
 */
@RestController
@RequestMapping("/admin/station")
public class AdminStationController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResponse>> queryPassengerList(@Valid StationQueryReq req) {
        PageResp<StationQueryResponse> stationList = stationService.queryList(req);
        return new CommonResp<>(stationList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable Long id){
        stationService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResponse>> queryList() {
        List<StationQueryResponse> list = stationService.queryAll();
        return new CommonResp<>(list);
    }
}
