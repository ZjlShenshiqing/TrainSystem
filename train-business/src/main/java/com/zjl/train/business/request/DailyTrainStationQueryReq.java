package com.zjl.train.business.request;

import com.zjl.train.common.request.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 每日车次经停站信息查询DTO
 * Created By Zhangjilin 2024/11/30
 */
public class DailyTrainStationQueryReq extends PageRequest {

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "DailyTrainStationQueryReq{" +
                "date=" + date +
                ", trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}
