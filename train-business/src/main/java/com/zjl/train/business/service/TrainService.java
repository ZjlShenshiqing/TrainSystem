package com.zjl.train.business.service;

import com.zjl.train.business.entity.Train;
import com.zjl.train.business.request.TrainQueryReq;
import com.zjl.train.business.request.TrainSaveReq;
import com.zjl.train.business.resp.TrainQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次信息服务接口
 * Created By Zhangjilin 2024/11/22
 */
@Service
public interface TrainService {
    /**
     * Created By Zhangjilin 2024/11/22
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(TrainSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/22
     * 查询列表：查询车次列表
     */
    PageResp<TrainQueryResponse> queryList(TrainQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/22
     * 根据id删除车次信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/22
     * 查询所有车次的信息
     */
    List<TrainQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/28
     * 通过车次编号，查询数据是否存在
     */
    Train selectByUnique(String trainCode);

    /**
     * (查询所有车次)直接查询数据库并返回，方便服务间调用
     * Created By Zhangjilin 2024/12/2
     */
    List<Train> selectAll();
}
