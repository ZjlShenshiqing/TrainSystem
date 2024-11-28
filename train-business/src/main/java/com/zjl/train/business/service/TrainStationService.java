package com.zjl.train.business.service;

import com.zjl.train.business.entity.TrainStation;
import com.zjl.train.business.request.TrainStationQueryReq;
import com.zjl.train.business.request.TrainStationSaveReq;
import com.zjl.train.business.resp.TrainStationQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次经停站信息服务接口
 * Created By Zhangjilin 2024/11/23
 */
@Service
public interface TrainStationService {
    /**
     * Created By Zhangjilin 2024/11/23
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(TrainStationSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/23
     * 查询列表：查询车次经停站信息列表
     */
    PageResp<TrainStationQueryResponse> queryList(TrainStationQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/23
     * 根据id删除车次经停站信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/23
     * 查询所有车次经停站的信息
     */
    List<TrainStationQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/28
     * 通过车次编号 + 站序 （数据库唯一键），查询数据是否存在
     */
    TrainStation selectByUnique(Integer index, String trainCode);

    /**
     * Created By Zhangjilin 2024/11/28
     * 通过车次编号 + 站名 （数据库唯一键），查询数据是否存在
     */
    TrainStation selectByUnique(String name, String trainCode);
}
