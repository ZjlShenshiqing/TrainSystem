package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.TrainSeat;
import com.zjl.train.business.entity.TrainSeatExample;
import com.zjl.train.business.mapper.TrainSeatCustomizableMapper;
import com.zjl.train.business.mapper.TrainSeatMapper;
import com.zjl.train.business.request.TrainSeatQueryReq;
import com.zjl.train.business.request.TrainSeatSaveReq;
import com.zjl.train.business.resp.TrainSeatQueryResponse;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次座位信息服务接口实现类
 * Created By Zhangjilin 2024/11/24
 */
@Service
public class TrainSeatServiceImpl implements TrainSeatService {

    @Autowired
    private TrainSeatMapper trainMapper;

    @Autowired
    private TrainSeatCustomizableMapper trainCustomizableMapper;

    @Override
    public void save(TrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        TrainSeat train = BeanUtil.copyProperties(req, TrainSeat.class);
        if (ObjectUtil.isNull(train.getId())) {
            //TODO 保存之前，先校验唯一键是否存在
            TrainSeat trainDB = trainCustomizableMapper.selectByUnique(req.getTrainCode());

            // 首先判断是否已经有同名的车站
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
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
    public PageResp<TrainSeatQueryResponse> queryList(TrainSeatQueryReq request) {
        // 查询条件类
        TrainSeatExample passengerExample = new TrainSeatExample();
        // 设置按 'id' 降序排序
        passengerExample.setOrderByClause("id desc");

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<TrainSeat> passengers = trainMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<TrainSeatQueryResponse> queryResponses = BeanUtil.copyToList(passengers, TrainSeatQueryResponse.class);
        PageResp<TrainSeatQueryResponse> passengerPageResp = new PageResp<>();

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
    public List<TrainSeatQueryResponse> queryAll() {
        TrainSeatExample trainExample = new TrainSeatExample();
        trainExample.setOrderByClause("name_pinyin asc");
        List<TrainSeat> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, TrainSeatQueryResponse.class);
    }
}
