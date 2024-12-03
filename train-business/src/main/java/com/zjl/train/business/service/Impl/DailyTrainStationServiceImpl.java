package com.zjl.DailyTrain.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.DailyTrainStation;
import com.zjl.train.business.entity.DailyTrainStationExample;
import com.zjl.train.business.entity.TrainStation;
import com.zjl.train.business.mapper.DailyTrainStationMapper;
import com.zjl.train.business.request.DailyTrainStationQueryReq;
import com.zjl.train.business.request.DailyTrainStationSaveReq;
import com.zjl.train.business.resp.DailyTrainStationQueryResponse;
import com.zjl.train.business.service.DailyTrainStationService;
import com.zjl.train.business.service.Impl.DailyTrainServiceImpl;
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
 * 车次经停站信息服务接口实现类
 * Created By Zhangjilin 2024/11/23
 */
@Service
public class DailyTrainStationServiceImpl implements DailyTrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainServiceImpl.class);

    @Autowired
    private DailyTrainStationMapper dailyTrainMapper;

    @Autowired
    private TrainStationService trainStationService;

    @Override
    public void save(DailyTrainStationSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainStation DailyTrain = BeanUtil.copyProperties(req, DailyTrainStation.class);
        if (ObjectUtil.isNull(DailyTrain.getId())) {
            // 保存之前，先校验车站是否存在 (校验1 车站名)
            DailyTrainStation DailyTrainStationDB1 = selectByUnique(req.getName(), req.getTrainCode());

            // 不为空，抛出异常
            if (ObjectUtil.isNotEmpty(DailyTrainStationDB1)) {
                //throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            // 保存之前，先校验车站是否存在 (校验2 站序)
            DailyTrainStation DailyTrainStationDB2 = selectByUnique(req.getIndex(), req.getTrainCode());

            // 不为空，抛出异常
            if (ObjectUtil.isNotEmpty(DailyTrainStationDB2)) {
                //throw new BusinessException(BusinessExceptionEnum.BUSINESS_Train_STATION_INDEX_UNIQUE_ERROR);
            }

            // 开始保存的操作，将信息插入到数据库中
            DailyTrain.setId(SnowUtil.getSnowflakeNextId());
            DailyTrain.setCreateTime(now);
            DailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(DailyTrain);
        } else {
            // 更新操作
            DailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(DailyTrain);
        }
    }


    @Override
    public PageResp<DailyTrainStationQueryResponse> queryList(DailyTrainStationQueryReq request) {
        // 查询条件类
        DailyTrainStationExample passengerExample = new DailyTrainStationExample();
        // 设置按 'id' 降序排序
        passengerExample.setOrderByClause("DailyTrain_code asc, `index` asc");

        /**
         * 如果请求中提供了 DailyTrainCode，则在查询条件中添加 DailyTrain_code 等于指定值的条件，
         * 筛选出特定车次的站点信息。
         */
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            passengerExample.createCriteria().andTrainCodeEqualTo(request.getTrainCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<DailyTrainStation> passengers = dailyTrainMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<DailyTrainStationQueryResponse> queryResponses = BeanUtil.copyToList(passengers, DailyTrainStationQueryResponse.class);
        PageResp<DailyTrainStationQueryResponse> passengerPageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        passengerPageResp.setTotal(pageInfo.getTotal());
        passengerPageResp.setList(queryResponses);
        return passengerPageResp;
    }

    @Override
    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DailyTrainStationQueryResponse> queryAll() {
        DailyTrainStationExample DailyTrainExample = new DailyTrainStationExample();
        DailyTrainExample.setOrderByClause("name_pinyin asc");
        List<DailyTrainStation> DailyTrainList = dailyTrainMapper.selectByExample(DailyTrainExample);
        return BeanUtil.copyToList(DailyTrainList, DailyTrainStationQueryResponse.class);
    }

    @Override
    public DailyTrainStation selectByUnique(Integer index, String DailyTrainCode) {
        DailyTrainStationExample DailyTrainStationExample = new DailyTrainStationExample();
        DailyTrainStationExample.createCriteria()
                .andTrainCodeEqualTo(DailyTrainCode)
                .andIndexEqualTo(index);
        List<DailyTrainStation> DailyTrainStations = dailyTrainMapper.selectByExample(DailyTrainStationExample);
        if (CollUtil.isNotEmpty(DailyTrainStations)) {
            return DailyTrainStations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public DailyTrainStation selectByUnique(String name, String DailyTrainCode) {
        DailyTrainStationExample DailyTrainStationExample = new DailyTrainStationExample();
        DailyTrainStationExample.createCriteria()
                .andTrainCodeEqualTo(DailyTrainCode)
                .andNameEqualTo(name);
        List<DailyTrainStation> DailyTrainStations = dailyTrainMapper.selectByExample(DailyTrainStationExample);
        if (CollUtil.isNotEmpty(DailyTrainStations)) {
            return DailyTrainStations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void autoDailyTrainStation(Date date, String trainCode) {
        LOG.info("开始自动生成每日车次经停站信息，日期：{}，车次：{}", DateUtil.formatDate(date), trainCode);
        // 先清空数据库的站点信息(某日车次)，再生成站点
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        dailyTrainMapper.deleteByExample(dailyTrainStationExample);

        // 查询当前车次下的所有站点信息
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);

        //查到经停站信息后，判断是否为空
        if (CollUtil.isEmpty(trainStations)) {
            LOG.info("没有经停站信息，无法生成每日车次经停站信息，日期：{}，车次：{}", date, trainCode);
            return;
        }

        for (TrainStation trainStation : trainStations) {
            // 生成每日车次经停站信息
            Date now = DateTime.now();
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setDate(date);
            dailyTrainMapper.insert(dailyTrainStation);
        }

        LOG.info("结束生成每日车次经停站信息，日期：{}，车次：{}", DateUtil.formatDate(date), trainCode);
    }
}
