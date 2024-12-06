package com.zjl.train.business.controller.web;

import com.zjl.train.business.resp.StationQueryResponse;
import com.zjl.train.business.service.StationService;
import com.zjl.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车站信息控制层 - 用户端
 * Created By Zhangjilin 2024/12/6
 */
@RestController
@RequestMapping("/station")
public class StationController {

    @Resource
    private StationService stationService;

    /**
     * 查询所有车次，返回给前端做子表的下拉框
     * Created By Zhangjilin 2024/12/6
     * @return
     */
    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResponse>> queryAll() {
        List<StationQueryResponse> list = stationService.queryAll();
        return new CommonResp<>(list);
    }
}
