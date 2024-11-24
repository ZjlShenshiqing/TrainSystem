package com.zjl.train.business.request;

import com.zjl.train.common.request.PageRequest;

/**
 * 车次车厢信息查询DTO
 * Created By Zhangjilin 2024/11/24
 */
public class TrainCarriageQueryReq extends PageRequest {

    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "TrainStationQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}
