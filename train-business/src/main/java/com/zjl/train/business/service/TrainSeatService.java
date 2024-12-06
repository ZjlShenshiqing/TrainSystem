package com.zjl.train.business.service;

import com.zjl.train.business.entity.TrainSeat;
import com.zjl.train.business.request.TrainSeatQueryReq;
import com.zjl.train.business.request.TrainSeatSaveReq;
import com.zjl.train.business.resp.TrainSeatQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次座位信息服务接口
 * Created By Zhangjilin 2024/11/24
 */
@Service
public interface TrainSeatService {
    /**
     * Created By Zhangjilin 2024/11/24
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(TrainSeatSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/24
     * 查询列表：查询车次座位信息列表
     */
    PageResp<TrainSeatQueryResponse> queryList(TrainSeatQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/24
     * 根据id删除车次座位信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/24
     * 查询所有车次座位的信息
     */
    List<TrainSeatQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/27
     * 通过车次生成座位
     */
    void autoTrainSeat(String trainCode);

    /**
     * Created By Zhangjilin 2024/12/3
     * 通过车次号查找座位
     */
    List<TrainSeat> selectByTrainCode(String trainCode);
}
