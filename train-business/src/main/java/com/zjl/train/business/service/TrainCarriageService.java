package com.zjl.train.business.service;

import com.zjl.train.business.request.TrainCarriageQueryReq;
import com.zjl.train.business.request.TrainCarriageSaveReq;
import com.zjl.train.business.resp.TrainCarriageQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次车厢信息服务接口
 * Created By Zhangjilin 2024/11/24
 */
@Service
public interface TrainCarriageService {
    /**
     * Created By Zhangjilin 2024/11/24
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(TrainCarriageSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/24
     * 查询列表：查询车次车厢信息列表
     */
    PageResp<TrainCarriageQueryResponse> queryList(TrainCarriageQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/24
     * 根据id删除车次车厢信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/24
     * 查询所有车次车厢的信息
     */
    List<TrainCarriageQueryResponse>  queryAll();
}
