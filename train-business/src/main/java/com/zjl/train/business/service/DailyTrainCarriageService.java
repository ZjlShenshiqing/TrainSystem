package com.zjl.train.business.service;

import com.zjl.train.business.entity.DailyTrainCarriage;
import com.zjl.train.business.request.DailyTrainCarriageQueryReq;
import com.zjl.train.business.request.DailyTrainCarriageSaveReq;
import com.zjl.train.business.resp.DailyTrainCarriageQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日车次车厢信息服务接口
 * Created By Zhangjilin 2024/12/1
 */
@Service
public interface DailyTrainCarriageService {
    /**
     * Created By Zhangjilin 2024/12/2
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(DailyTrainCarriageSaveReq request);

    /**
     * Created By Zhangjilin 2024/12/2
     * 查询列表：查询车次车厢信息列表
     */
    PageResp<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryReq request);

    /**
     * Created By Zhangjilin 2024/12/2
     * 根据id删除车次车厢信息
     * @param id
     */
    public void delete(Long id);

    /**
     * Created By Zhangjilin 2024/12/2
     * 查询所有车次车厢的信息
     */
    List<DailyTrainCarriageQueryResponse>  queryAll();

    /**
     * Created By Zhangjilin 2024/11/27
     * 通过车次查找车厢
     */
    List<DailyTrainCarriage> selectByTrainCode(String trainCode);

    /**
     * Created By Zhangjilin 2024/11/28
     * 通过车次编号 + 车厢号 （数据库唯一键），查询数据是否存在
     */
    DailyTrainCarriage selectByUnique(String index, Integer trainCode);
}
