package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.DailyTrain;
import com.zjl.train.business.entity.DailyTrainExample;
import com.zjl.train.business.entity.Train;
import com.zjl.train.business.mapper.DailyTrainMapper;
import com.zjl.train.business.request.DailyTrainQueryReq;
import com.zjl.train.business.request.DailyTrainSaveReq;
import com.zjl.train.business.resp.DailyTrainQueryResponse;
import com.zjl.train.business.service.*;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 每日车次信息服务接口实现类
 * Created By Zhangjilin 2024/11/30
 */
@Service
public class DailyTrainServiceImpl implements DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainServiceImpl.class);

    @Autowired
    private DailyTrainMapper dailyTrainMapper;

    @Autowired
    private TrainService trainService;

    @Autowired
    private DailyTrainStationService dailyTrainStationService;

    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @Autowired
    private DailyTrainTicketService dailyTrainTicketService;

    @Override
    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain train = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(train.getId())) {

            // 保存之前，先校验车站是否存在
            DailyTrain trainDB = selectByUnique(req.getCode());

            // 不为空，抛出异常
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }

            // 开始保存的操作，将信息插入到数据库中
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            dailyTrainMapper.insert(train);
        } else {
            // 更新操作
            train.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(train);
        }
    }


    @Override
    public PageResp<DailyTrainQueryResponse> queryList(DailyTrainQueryReq request) {
        // 查询条件类
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        // 设置按 'id' 降序排序
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        
        // 加入日期作为筛选条件
        if (ObjectUtil.isNotNull(request.getDate())) {
            criteria.andDateEqualTo(request.getDate());
        }
        
        // 加入车次作为搜索条件
        if (ObjectUtil.isNotEmpty(request.getCode())) {
            criteria.andCodeEqualTo(request.getCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectByExample(dailyTrainExample);

        // 获取总条数
        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrains);

        // 封装成查询的响应值
        List<DailyTrainQueryResponse> queryResponses = BeanUtil.copyToList(dailyTrains, DailyTrainQueryResponse.class);
        PageResp<DailyTrainQueryResponse> dailyTrainQueryResponsePageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        dailyTrainQueryResponsePageResp.setTotal(pageInfo.getTotal());
        dailyTrainQueryResponsePageResp.setList(queryResponses);
        return dailyTrainQueryResponsePageResp;
    }

    @Override
    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DailyTrainQueryResponse> queryAll() {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("id asc");
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);
        return BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResponse.class);
    }

    @Override
    public DailyTrain selectByUnique(String trainCode) {
        DailyTrainExample trainExample = new DailyTrainExample();
        trainExample.createCriteria().andCodeEqualTo(trainCode);
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(trainExample);
        if (ObjectUtil.isNotEmpty(dailyTrainList)) {
            return dailyTrainList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void genDaily(Date date) {
        // 拿到数据库所有车次的信息
        List<Train> trains = trainService.selectAll();
        // 注意：以后遇到List，要学会判空！  这样不会出现空指针异常
        if (CollUtil.isEmpty(trains)) {
            LOG.info("没有基础车次数据，任务结束");
            return;
        }

        for (Train train : trains) {
            genDailyTrain(date, train);
        }
    }

    @Override
    @Transactional
    public void genDailyTrain(Date date, Train train) {
        LOG.info("开始生成每日车次信息，日期：{}", DateUtil.formatDate(date));
        // 重复生成前，应该把数据库清空（删除已有的数据）
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        criteria.andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

        // 生成某一车次的数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);

        // 生成每日车次经停站信息
        dailyTrainStationService.autoDailyTrainStation(date, train.getCode());

        // 生成每日车次车厢信息
        dailyTrainCarriageService.autoDailyTrainCarriage(date, train.getCode());

        // 生成每日车次座位信息
        dailyTrainSeatService.autoTrainSeat(train.getCode() , date);

        // 生成每日余票信息
        dailyTrainTicketService.autoDailyTicket(dailyTrain, date, train.getCode());

        LOG.info("结束生成每日车次信息，日期：{}", DateUtil.formatDate(date));
    }
}
