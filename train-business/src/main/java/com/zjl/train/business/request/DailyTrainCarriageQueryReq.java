package com.zjl.train.business.request;

import com.zjl.train.common.request.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * 每日车次车厢信息查询DTO
 * Created By Zhangjilin 2024/12/2
 */
public class DailyTrainCarriageQueryReq extends PageRequest {

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 加入trainCode，筛选车次信息
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
        return "DailyTrainCarriageQueryReq{" +
                "date=" + date +
                ", trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}
