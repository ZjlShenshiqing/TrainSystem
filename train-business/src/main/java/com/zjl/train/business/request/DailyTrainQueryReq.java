package com.zjl.train.business.request;

import com.zjl.train.common.request.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 每日车次信息查询DTO
 * Created By Zhangjilin 2024/11/30
 */
public class DailyTrainQueryReq extends PageRequest {

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
