package com.zjl.train.business.service;

import com.zjl.train.business.request.DailyTrainSeatQueryReq;
import com.zjl.train.business.request.DailyTrainSeatSaveReq;
import com.zjl.train.business.resp.DailyTrainSeatQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 每日车次座位信息服务接口
 * Created By Zhangjilin 2024/12/2
 */
@Service
public interface DailyTrainSeatService {
    /**
     * Created By Zhangjilin 2024/12/2
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(DailyTrainSeatSaveReq request);

    /**
     * Created By Zhangjilin 2024/12/2
     * 查询列表：查询每日车次座位信息列表
     */
    PageResp<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryReq request);

    /**
     * Created By Zhangjilin 2024/12/2
     * 根据id删除车次座位信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/12/2
     * 查询所有车次座位的信息
     */
    List<DailyTrainSeatQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/12/2
     * 通过车次生成座位
     */
    void autoTrainSeat(String trainCode , Date date);
}
