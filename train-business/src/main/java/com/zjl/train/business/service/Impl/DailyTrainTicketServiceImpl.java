package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.zjl.train.business.entity.DailyTrain;
import com.zjl.train.business.entity.DailyTrainTicket;
import com.zjl.train.business.entity.DailyTrainTicketExample;
import com.zjl.train.business.entity.TrainStation;
import com.zjl.train.business.enums.SeatTypeEnum;
import com.zjl.train.business.enums.TrainTypeEnum;
import com.zjl.train.business.mapper.DailyTrainTicketMapper;
import com.zjl.train.business.request.DailyTrainTicketQueryReq;
import com.zjl.train.business.request.DailyTrainTicketSaveReq;
import com.zjl.train.business.resp.DailyTrainTicketQueryResponse;
import com.zjl.train.business.service.DailyTrainSeatService;
import com.zjl.train.business.service.DailyTrainTicketService;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.business.service.TrainStationService;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 余票查询服务实现类 - 管理端
 * Created By Zhangjilin 2024/12/4
 */
@Service
public class DailyTrainTicketServiceImpl implements DailyTrainTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Autowired
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Autowired
    private TrainStationService trainStationService;

    @Autowired
    private TrainSeatService trainSeatService;

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @Override
    public void save(DailyTrainTicketSaveReq request) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(request, DailyTrainTicket.class);
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }
    }

    @Override
    public PageResp<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryReq request) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        /**
         * 查询的条件，有如下四个
         */
        if (ObjUtil.isNotNull(request.getDate())) {
            criteria.andDateEqualTo(request.getDate());
        }
        if (ObjUtil.isNotEmpty(request.getTrainCode())) {
            criteria.andTrainCodeEqualTo(request.getTrainCode());
        }
        if (ObjUtil.isNotEmpty(request.getStart())) {
            criteria.andStartEqualTo(request.getStart());
        }
        if (ObjUtil.isNotEmpty(request.getEnd())) {
            criteria.andEndEqualTo(request.getEnd());
        }

        LOG.info("查询页码：{}", request.getPage());
        LOG.info("每页条数：{}", request.getSize());
        PageHelper.startPage(request.getPage(), request.getSize());
        // TODO
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        return null;
    }

    @Override
    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }


    @Transactional // 批量任务：要么全部成功要么全部失败
    @Override
    public void autoDailyTicket(DailyTrain dailyTrain, Date date, String trainCode) {
        LOG.info("开始自动生成每日余票信息，日期：{}，车次：{}", DateUtil.formatDate(date), trainCode);

        // 先清空数据库的座位信息，再生成座位
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

        // 查询途径的车站信息
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainStations)) {
            LOG.info("该车次没有车站的基础数据，生成该车次的车站信息结束");
            return;
        }

        DateTime now = DateTime.now();

        /**
         * 生成每日余票信息
         * 业务1：计算里程
         * 业务2：计算票价
         * 业务3：生成座位信息
         */
        // 使用单向嵌套循环来生成 因为不能反过来
        for (int i = 0; i < trainStations.size(); i++) {
            // 得到出发站
            TrainStation trainStationStart = trainStations.get(i);

            /**
             * 计算起始站到终点站的历程，起始站 = 0 比如现在是深圳北
             * 这是叠加的，而不是一次计算出来的
             */
            BigDecimal sumKM = BigDecimal.ZERO;

            for (int j = (i + 1); j < trainStations.size(); j++) {
                // 得到终点站 （其实是下一站，根据循环轮次的增加，距离变长） 现在是虎门 再下一次就是广州南
                TrainStation trainStationEnd = trainStations.get(j);

                sumKM = sumKM.add(trainStationEnd.getKm()); // 叠加，计算里程之和

                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());

                // 下面是和座位的信息产生了强关联 ，所以要查座位信息
                // 通过dailyTrainService的count方法，查出四种不同的座位类型的信息
                int ydz = Math.toIntExact(dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YDZ.getCode()));
                int edz = Math.toIntExact(dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.EDZ.getCode()));
                int yw = Math.toIntExact(dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YW.getCode()));
                int rw = Math.toIntExact(dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.RW.getCode()));

                // 票价 = 里程之和 * 座位单价 * 车次类型系数
                String trainType = dailyTrain.getType();

                // 获取指定车次类型的字段 - 车次类型系数
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);

                BigDecimal ydzPrice = sumKM.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKM.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = sumKM.multiply(SeatTypeEnum.RW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKM.multiply(SeatTypeEnum.YW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);

                dailyTrainTicket.setYdz(ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rw);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(yw);
                dailyTrainTicket.setYwPrice(rwPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }

        LOG.info("结束自动生成每日余票信息，日期：{}，车次：{}", DateUtil.formatDate(date), trainCode);
    }
}
