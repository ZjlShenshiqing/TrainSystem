package com.zjl.train.batchtask.openFeign;

import com.zjl.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@FeignClient(name = "business", url = "http://127.0.0.1:8002/business")
public interface BusinessFeign {

    /**
     * Feign调用：自动通过车次生成某日所有车次功能
     * Created By Zhangjilin 2024/12/2
     */
    @GetMapping("/admin/daily-train/genDaily/{date}")
    CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
