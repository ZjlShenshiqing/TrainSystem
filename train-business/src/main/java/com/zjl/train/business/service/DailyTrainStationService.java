package com.zjl.train.business.service;

import com.zjl.train.business.entity.DailyTrainStation;
import com.zjl.train.business.request.DailyTrainStationQueryReq;
import com.zjl.train.business.request.DailyTrainStationSaveReq;
import com.zjl.train.business.resp.DailyTrainStationQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日车次经停站信息服务接口
 * Created By Zhangjilin 2024/11/30
 */
@Service
public interface DailyTrainStationService {
    /**
     * Created By Zhangjilin 2024/11/30
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(DailyTrainStationSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/30
     * 查询列表：查询每日车次经停站信息列表
     */
    PageResp<DailyTrainStationQueryResponse> queryList(DailyTrainStationQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/30
     * 根据id删除每日车次经停站信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/30
     * 查询所有每日车次经停站的信息
     */
    List<DailyTrainStationQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/30
     * 通过每日车次编号 + 站序 （数据库唯一键），查询数据是否存在
     */
    DailyTrainStation selectByUnique(Integer index, String trainCode);

    /**
     * Created By Zhangjilin 2024/11/30
     * 通过每日车次编号 + 站名 （数据库唯一键），查询数据是否存在
     */
    DailyTrainStation selectByUnique(String name, String trainCode);
}
