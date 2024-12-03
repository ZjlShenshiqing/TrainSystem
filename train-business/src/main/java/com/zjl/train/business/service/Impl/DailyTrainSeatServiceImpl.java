package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.DailyTrainSeat;
import com.zjl.train.business.entity.DailyTrainSeatExample;
import com.zjl.train.business.entity.TrainSeat;
import com.zjl.train.business.entity.TrainStation;
import com.zjl.train.business.mapper.DailyTrainSeatMapper;
import com.zjl.train.business.request.DailyTrainSeatQueryReq;
import com.zjl.train.business.request.DailyTrainSeatSaveReq;
import com.zjl.train.business.resp.DailyTrainSeatQueryResponse;
import com.zjl.train.business.service.DailyTrainSeatService;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.business.service.TrainStationService;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 每日车次座位信息服务接口实现类
 * Created By Zhangjilin 2024/12/2
 */
@Service
public class DailyTrainSeatServiceImpl implements DailyTrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainServiceImpl.class);

    @Autowired
    private DailyTrainSeatMapper trainMapper;

    @Autowired
    private TrainSeatService trainSeatService;

    @Autowired
    private TrainStationService trainStationService;

    /**
     * 方案已弃用，该成由车次自动生成
     * Created By Zhangjilin 2024/11/28
     * @param req
     */
    @Override
    @Deprecated
    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat trainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        if (ObjectUtil.isNull(trainSeat.getId())) {
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainMapper.insert(trainSeat);
        } else {
            trainSeat.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(trainSeat);
        }
    }


    @Override
    public PageResp<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryReq request) {
        // 查询条件类
        DailyTrainSeatExample dailySeatExample = new DailyTrainSeatExample();
        // 设置按 'id' 降序排序
        dailySeatExample.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");

        DailyTrainSeatExample.Criteria criteria = dailySeatExample.createCriteria();
        // 判断是否有车次信息
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            criteria.andTrainCodeEqualTo(request.getTrainCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<DailyTrainSeat> dailySeats = trainMapper.selectByExample(dailySeatExample);

        // 获取总条数
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailySeats);

        // 封装成查询的响应值
        List<DailyTrainSeatQueryResponse> queryResponses = BeanUtil.copyToList(dailySeats, DailyTrainSeatQueryResponse.class);
        PageResp<DailyTrainSeatQueryResponse> dailySeatPageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        dailySeatPageResp.setTotal(pageInfo.getTotal());
        dailySeatPageResp.setList(queryResponses);
        return dailySeatPageResp;
    }

    @Override
    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DailyTrainSeatQueryResponse> queryAll() {
        DailyTrainSeatExample trainExample = new DailyTrainSeatExample();
        trainExample.setOrderByClause("name_pinyin asc");
        List<DailyTrainSeat> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, DailyTrainSeatQueryResponse.class);
    }


    /**
     * 注意：需要增加售卖信息
     * @param trainCode
     * @param date
     */
    @Override
    public void autoTrainSeat(String trainCode, Date date) {
        LOG.info("开始自动生成每日车次座位信息，日期：{}，车次：{}", DateUtil.formatDate(date), trainCode);
        // 先清空数据库的座位信息，再生成座位
        DailyTrainSeatExample trainSeatExample = new DailyTrainSeatExample();
        DailyTrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        trainMapper.deleteByExample(trainSeatExample);

        // 查询当前车次下的所有经停站
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        // 生成售卖信息
        String sell = StrUtil.fillBefore("",'0',trainStations.size() - 1);

        // 查询当前车次下的所有车厢
        List<TrainSeat> trainSeats = trainSeatService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainSeats)) {
            LOG.info("车次：{}，没有座位信息", trainCode);
            return;
        }

        // 生成座位信息
        for (TrainSeat trainSeat : trainSeats) {
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setSell(sell);
            trainMapper.insert(dailyTrainSeat);
        }
        LOG.info("开始自动生成每日车次座位信息，日期：{}，车次：{}", DateUtil.formatDate(date), trainCode);
    }
}
