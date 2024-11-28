package com.zjl.train.business.service;

import com.zjl.train.business.entity.Station;
import com.zjl.train.business.request.StationQueryReq;
import com.zjl.train.business.request.StationSaveReq;
import com.zjl.train.business.resp.StationQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车站信息服务接口
 * Created By Zhangjilin 2024/11/20
 */
@Service
public interface StationService {
    /**
     * Created By Zhangjilin 2024/11/21
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(StationSaveReq request);

    /**
     * Created By Zhangjilin 2024/11/21
     * 查询列表：查询站点列表
     */
    public PageResp<StationQueryResponse> queryList(StationQueryReq request);

    /**
     * Created By Zhangjilin 2024/11/21
     * 根据id删除站点信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/11/21
     * 查询所有站点的信息,给前端返回下拉框
     */
    List<StationQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/28
     * 通过车站名字，查询数据是否存在
     */
    Station selectByUnique(String stationName);
}
