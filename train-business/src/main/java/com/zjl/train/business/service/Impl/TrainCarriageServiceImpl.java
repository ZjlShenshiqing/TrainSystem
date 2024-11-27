package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.TrainCarriage;
import com.zjl.train.business.entity.TrainCarriageExample;
import com.zjl.train.business.mapper.TrainCarriageCustomizableMapper;
import com.zjl.train.business.mapper.TrainCarriageMapper;
import com.zjl.train.business.request.TrainCarriageQueryReq;
import com.zjl.train.business.request.TrainCarriageSaveReq;
import com.zjl.train.business.resp.TrainCarriageQueryResponse;
import com.zjl.train.business.service.TrainCarriageService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次车厢信息服务接口实现类
 * Created By Zhangjilin 2024/11/24
 */
@Service
public class TrainCarriageServiceImpl implements TrainCarriageService {

    @Autowired
    private TrainCarriageMapper trainMapper;

    @Autowired
    private TrainCarriageCustomizableMapper trainCustomizableMapper;

    @Override
    public void save(TrainCarriageSaveReq req) {
        DateTime now = DateTime.now();
        TrainCarriage train = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(train.getId())) {
            //TODO 保存之前，先校验唯一键是否存在
            TrainCarriage trainDB = trainCustomizableMapper.selectByUnique(req.getTrainCode());

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
    public PageResp<TrainCarriageQueryResponse> queryList(TrainCarriageQueryReq request) {
        // 查询条件类
        TrainCarriageExample passengerExample = new TrainCarriageExample();
        // 设置按 'id' 降序排序
        passengerExample.setOrderByClause("train_code asc, `index` asc");

        /**
         * 筛选车次
         */
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            passengerExample.createCriteria().andTrainCodeEqualTo(request.getTrainCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<TrainCarriage> passengers = trainMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<TrainCarriageQueryResponse> queryResponses = BeanUtil.copyToList(passengers, TrainCarriageQueryResponse.class);
        PageResp<TrainCarriageQueryResponse> passengerPageResp = new PageResp<>();

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
    public List<TrainCarriageQueryResponse> queryAll() {
        TrainCarriageExample trainExample = new TrainCarriageExample();
        trainExample.setOrderByClause("name_pinyin asc");
        List<TrainCarriage> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, TrainCarriageQueryResponse.class);
    }

    @Override
    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        List<TrainCarriage> trainCarriages = trainMapper.selectByExample(trainCarriageExample);
        return trainCarriages;
    }
}
