package com.zjl.train.business.service;

import com.zjl.train.business.entity.DailyTrain;
import com.zjl.train.business.request.DailyTrainQueryReq;
import com.zjl.train.business.request.DailyTrainSaveReq;
import com.zjl.train.business.resp.DailyTrainQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日车次信息服务接口
 * Created By Zhangjilin 2024/11/30
 */
@Service
public interface DailyTrainService {
    /**
     * Created By Zhangjilin 2024/11/30
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(DailyTrainSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/30
     * 查询列表：查询每日车次列表
     */
    PageResp<DailyTrainQueryResponse> queryList(DailyTrainQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/30
     * 根据id删除每日车次信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/30
     * 查询所有每日车次的信息
     */
    List<DailyTrainQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/30
     * 通过车次编号，查询每日车次数据是否存在
     */
    DailyTrain selectByUnique(String trainCode);
}
