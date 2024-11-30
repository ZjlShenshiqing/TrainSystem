package com.zjl.train.business.request;

import com.zjl.train.common.request.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 每日车次信息查询DTO
 * Created By Zhangjilin 2024/11/30
 */
public class DailyTrainQueryReq extends PageRequest {

    /**
     * 查询参数加入日期查询和车次作为筛选
     * 注意：这里的日期是get请求，是拼接到url里面的，不能使用JSONTimeFormat的注解，只能使用这个DateTimeFormat注解
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String code;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "StationQueryReq{" +
                "} " + super.toString();
    }
}
