package com.zjl.train.business.request;

import com.zjl.train.common.request.PageRequest;

/**
 * 每日车次座位信息查询DTO
 * Created By Zhangjilin 2024/12/2
 */
public class DailyTrainSeatQueryReq extends PageRequest {

    /**
     * 加入车次来进行筛选
     */
    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "TrainSeatQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}
