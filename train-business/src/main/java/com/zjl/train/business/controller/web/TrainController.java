package com.zjl.train.business.controller.web;

import com.zjl.train.business.resp.TrainQueryResponse;
import com.zjl.train.business.service.TrainService;
import com.zjl.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车次信息控制层 - 用户端
 * Created By Zhangjilin 2024/12/6
 */
@RestController
@RequestMapping("/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResponse>> queryList() {
        List<TrainQueryResponse> list = trainService.queryAll();
        return new CommonResp<>(list);
    }
}
