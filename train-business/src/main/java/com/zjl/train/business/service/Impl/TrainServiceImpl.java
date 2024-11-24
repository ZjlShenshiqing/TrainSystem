package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.Train;
import com.zjl.train.business.entity.TrainExample;
import com.zjl.train.business.mapper.TrainCustomizableMapper;
import com.zjl.train.business.mapper.TrainMapper;
import com.zjl.train.business.request.TrainQueryReq;
import com.zjl.train.business.request.TrainSaveReq;
import com.zjl.train.business.resp.TrainQueryResponse;
import com.zjl.train.business.service.TrainService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次信息服务接口实现类
 * Created By Zhangjilin 2024/11/22
 */
@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainMapper trainMapper;

    @Autowired
    private TrainCustomizableMapper trainCustomizableMapper;

    @Override
    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        if (ObjectUtil.isNull(train.getId())) {
            //TODO 保存之前，先校验唯一键是否存在
            Train trainDB = trainCustomizableMapper.selectByUnique(req.getEnd());

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
    public PageResp<TrainQueryResponse> queryList(TrainQueryReq request) {
        // 查询条件类
        TrainExample passengerExample = new TrainExample();
        // 设置按 'id' 降序排序
        passengerExample.setOrderByClause("id desc");

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<Train> passengers = trainMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<Train> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<TrainQueryResponse> queryResponses = BeanUtil.copyToList(passengers, TrainQueryResponse.class);
        PageResp<TrainQueryResponse> passengerPageResp = new PageResp<>();

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
    public List<TrainQueryResponse> queryAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id asc");
        List<Train> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, TrainQueryResponse.class);
    }
}
