package com.zjl.train.business.service.Impl;

import com.zjl.train.business.entity.ConfirmOrder;
import com.zjl.train.business.entity.DailyTrainSeat;
import com.zjl.train.business.entity.DailyTrainTicket;
import com.zjl.train.business.mapper.ConfirmOrderMapper;
import com.zjl.train.business.request.ConfirmOrderDoReq;
import com.zjl.train.business.request.ConfirmOrderQueryReq;
import com.zjl.train.business.resp.ConfirmOrderQueryResponse;
import com.zjl.train.business.service.ConfirmOrderService;
import com.zjl.train.common.resp.PageResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 确认订单 - 服务层实现类
 * Created By Zhangjilin 2024/12/9
 */
@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Autowired
    private ConfirmOrderMapper confirmOrderMapper;


    @Override
    public void save(ConfirmOrderDoReq req) {

    }

    @Override
    public PageResp<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryReq req) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void doConfirm() {

    }

    @Override
    public void updateStatus(ConfirmOrder confirmOrder) {

    }

    @Override
    public void sell(ConfirmOrder confirmOrder) {

    }

    @Override
    public void getSeat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode, String seatType, String column, List<Integer> offsetList, Integer startIndex, Integer endIndex) {

    }

    @Override
    public boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        return false;
    }

    @Override
    public void reduceTickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {

    }

    @Override
    public void doConfirmBlock(ConfirmOrderDoReq req) {

    }

    @Override
    public Integer queryLineCount(Long id) {
        return 0;
    }

    @Override
    public Integer cancel(Long id) {
        return 0;
    }
}
