package com.zjl.train.business.controller.admin;

import com.zjl.train.business.resp.StationQueryResponse;
import com.zjl.train.business.service.StationService;
import com.zjl.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车站信息控制层 - 管理端·
 * Created By Zhangjilin 2024/11/20
 */
@RestController
@RequestMapping("/admin/station")
public class AdminStationController {

    @Resource
    private StationService stationService;

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResponse>> queryList() {
        List<StationQueryResponse> list = stationService.queryAll();
        return new CommonResp<>(list);
    }
}
