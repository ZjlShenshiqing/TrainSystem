package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.TrainStation;
import com.zjl.train.business.entity.TrainStationExample;
import com.zjl.train.business.mapper.TrainStationMapper;
import com.zjl.train.business.request.TrainStationQueryReq;
import com.zjl.train.business.request.TrainStationSaveReq;
import com.zjl.train.business.resp.TrainStationQueryResponse;
import com.zjl.train.business.service.TrainStationService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次经停站信息服务接口实现类
 * Created By Zhangjilin 2024/11/23
 */
@Service
public class TrainStationServiceImpl implements TrainStationService {

    @Autowired
    private TrainStationMapper trainMapper;

    @Override
    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation train = BeanUtil.copyProperties(req, TrainStation.class);
        if (ObjectUtil.isNull(train.getId())) {
            // 保存之前，先校验车站是否存在 (校验1 车站名)
            TrainStation trainStationDB1 = selectByUnique(req.getName(), req.getTrainCode());

            // 不为空，抛出异常
            if (ObjectUtil.isNotEmpty(trainStationDB1)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            // 保存之前，先校验车站是否存在 (校验2 站序)
            TrainStation trainStationDB2 = selectByUnique(req.getIndex(), req.getTrainCode());

            // 不为空，抛出异常
            if (ObjectUtil.isNotEmpty(trainStationDB2)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }

            // 开始保存的操作，将信息插入到数据库中
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            // 更新操作
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }


    @Override
    public PageResp<TrainStationQueryResponse> queryList(TrainStationQueryReq request) {
        // 查询条件类
        TrainStationExample passengerExample = new TrainStationExample();
        // 设置按 'id' 降序排序
        passengerExample.setOrderByClause("train_code asc, `index` asc");

        /**
         * 如果请求中提供了 trainCode，则在查询条件中添加 train_code 等于指定值的条件，
         * 筛选出特定车次的站点信息。
         */
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            passengerExample.createCriteria().andTrainCodeEqualTo(request.getTrainCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<TrainStation> passengers = trainMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<TrainStation> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<TrainStationQueryResponse> queryResponses = BeanUtil.copyToList(passengers, TrainStationQueryResponse.class);
        PageResp<TrainStationQueryResponse> passengerPageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        passengerPageResp.setTotal(pageInfo.getTotal());
        passengerPageResp.setList(queryResponses);
        return passengerPageResp;
    }

    @Override
    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TrainStationQueryResponse> queryAll() {
        TrainStationExample trainExample = new TrainStationExample();
        trainExample.setOrderByClause("name_pinyin asc");
        List<TrainStation> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, TrainStationQueryResponse.class);
    }

    @Override
    public TrainStation selectByUnique(Integer index, String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        List<TrainStation> trainStations = trainMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(trainStations)) {
            return trainStations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public TrainStation selectByUnique(String name, String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andNameEqualTo(name);
        List<TrainStation> trainStations = trainMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(trainStations)) {
            return trainStations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<TrainStation> selectByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("`index` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        /**
         * Mybatis查询列表，如果没数据返回的是空列表，而不是null
         * 这样可以避免空指针异常，省得我们再去判断List是否为空了
         */
        List<TrainStation> trainStations = trainMapper.selectByExample(trainStationExample);
        return trainStations;
    }
}
