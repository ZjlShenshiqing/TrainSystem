package com.zjl.train.business.service;

import com.zjl.train.business.entity.ConfirmOrder;
import com.zjl.train.business.entity.DailyTrainSeat;
import com.zjl.train.business.entity.DailyTrainTicket;
import com.zjl.train.business.request.ConfirmOrderDoReq;
import com.zjl.train.business.request.ConfirmOrderQueryReq;
import com.zjl.train.business.resp.ConfirmOrderQueryResponse;
import com.zjl.train.common.resp.PageResp;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 确认订单 - 服务接口
 * Created By Zhangjilin 2024/12/9
 */
@Service
public interface ConfirmOrderService {

    /**
     * 保存订单
     * Created By Zhangjilin 2024/12/9
     */
    void save(ConfirmOrderDoReq req);

    /**
     * 查询订单列表
     * Created By Zhangjilin 2024/12/9
     */
    PageResp<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryReq req);

    /**
     * 删除订单
     * Created By Zhangjilin 2024/12/9
     */
    void delete(Long id);

    /**
     * Created By Zhangjilin 2024/12/9
     */
    void doConfirm(ConfirmOrderDoReq confirmOrderDoReq);

    /**
     * 更新订单状态
     * Created By Zhangjilin 2024/12/9
     */
    void updateStatus(ConfirmOrder confirmOrder);

    /**
     * 售票
     * Created By Zhangjilin 2024/12/9
     */
    void sell(ConfirmOrder confirmOrder);

    /**
     * 选座
     * Created By Zhangjilin 2024/12/9
     */
    void getSeat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode, String seatType, String column, List<Integer> offsetList, Integer startIndex, Integer endIndex);

    /**
     * 计算票在区间内是否可卖
     * Created By Zhangjilin 2024/12/9
     */
    boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex);

    /**
     * 扣减库存
     * Created By Zhangjilin 2024/12/9
     */
    void reduceTickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket);

    /**
     * 限流购票请求
     * Created By Zhangjilin 2024/12/9
     */
    void doConfirmBlock(ConfirmOrderDoReq req);

    /**
     * 查询排队人数
     * Created By Zhangjilin 2024/12/9
     */
    Integer queryLineCount(Long id);

    /**
     * 取消排队
     * Created By Zhangjilin 2024/12/9
     */
    Integer cancel(Long id);
}
