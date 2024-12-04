package com.zjl.train.business.service;

import com.zjl.train.business.entity.DailyTrain;
import com.zjl.train.business.entity.DailyTrainTicket;
import com.zjl.train.business.entity.Train;
import com.zjl.train.business.request.DailyTrainQueryReq;
import com.zjl.train.business.request.DailyTrainSaveReq;
import com.zjl.train.business.request.DailyTrainTicketQueryReq;
import com.zjl.train.business.request.DailyTrainTicketSaveReq;
import com.zjl.train.business.resp.DailyTrainQueryResponse;
import com.zjl.train.business.resp.DailyTrainTicketQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 每日余票信息服务接口
 * Created By Zhangjilin 2024/12/4
 */
@Service
public interface DailyTrainTicketService {
    /**
     * Created By Zhangjilin 2024/12/4
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(DailyTrainTicketSaveReq request);

    /**
     * Created By Zhangjilin 2024/12/4
     * 查询列表：查询每日余票列表
     */
    PageResp<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryReq request);

    /**
     * Created By Zhangjilin 2024/12/4
     * 根据id删除每日余票信息
     * @param id
     */
    public void delete(Long id);


    /**
     * Created By Zhangjilin 2024/12/4
     * 生成某日某车次车票的数据
     */
    void genDailyTicket(Date date, Train train);
}
