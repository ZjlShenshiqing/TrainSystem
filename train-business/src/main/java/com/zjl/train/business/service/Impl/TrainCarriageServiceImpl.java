package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.TrainCarriage;
import com.zjl.train.business.entity.TrainCarriageExample;
import com.zjl.train.business.enums.SeatColEnum;
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

        // 自动计算出列数 + 总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType()); // 通过座位类型知道有几列
        req.setColCount(seatColEnums.size());

        // 计算座位数
        req.setSeatCount(req.getColCount() * req.getRowCount());

        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(trainCarriage.getId())) {
            // 保存之前，先校验车厢是否存在
            TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());

            // 不为空，抛出异常
            if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }

            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainMapper.insert(trainCarriage);
        }
        else {
            trainCarriage.setUpdateTime(now);
            trainMapper.updateByPrimaryKeySelective(trainCarriage);
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

    @Override
    public TrainCarriage selectByUnique(String trainCode, Integer index) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.createCriteria().andIndexEqualTo(index).andTrainCodeEqualTo(trainCode);
        List<TrainCarriage> trainCarriage = trainMapper.selectByExample(trainCarriageExample);
        if (CollUtil.isNotEmpty(trainCarriage)){
            return trainCarriage.get(0);
        } else {
            return null;
        }
    }
}
