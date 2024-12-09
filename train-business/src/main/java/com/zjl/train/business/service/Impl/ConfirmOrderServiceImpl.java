package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
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
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    @Override
    public PageResp<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryReq req) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void doConfirm(ConfirmOrderDoReq confirmOrderDoReq) {
        // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否在有效期内，tickets条数>0，同乘客同车次是否已买过
        // 保存确认订单表，状态初始
        ConfirmOrder confirmOrder = new ConfirmOrder();

        // confirmOrderMapper.insert()
        // 查出余票记录，需要得到真实的库存

        // 扣减余票数量，并判断余票是否足够

        // 选座

        // 一个车箱一个车箱的获取座位数据

        // 挑选符合条件的座位，如果这个车箱不满足，则进入下个车箱（多个选座应该在同一个车厢）

        // 选中座位后事务处理：

        // 座位表修改售卖情况sell；
        // 余票详情表修改余票；
        // 为会员增加购票记录
        // 更新确认订单为成功
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
