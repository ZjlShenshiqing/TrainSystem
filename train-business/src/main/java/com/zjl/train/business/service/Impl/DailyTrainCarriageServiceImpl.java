package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.DailyTrainCarriage;
import com.zjl.train.business.entity.DailyTrainCarriageExample;
import com.zjl.train.business.enums.SeatColEnum;
import com.zjl.train.business.mapper.DailyTrainCarriageMapper;
import com.zjl.train.business.request.DailyTrainCarriageQueryReq;
import com.zjl.train.business.request.DailyTrainCarriageSaveReq;
import com.zjl.train.business.resp.DailyTrainCarriageQueryResponse;
import com.zjl.train.business.service.DailyTrainCarriageService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 每日车次车厢信息服务接口实现类
 * Created By Zhangjilin 2024/12/1
 */
@Service
public class DailyTrainCarriageServiceImpl implements DailyTrainCarriageService {

    @Autowired
    private DailyTrainCarriageMapper trainMapper;


    @Override
    public void save(DailyTrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 自动计算出列数 + 总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType()); // 通过座位类型知道有几列
        req.setColCount(seatColEnums.size());

        // 计算座位数
        req.setSeatCount(req.getColCount() * req.getRowCount());

        DailyTrainCarriage trainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        if (ObjectUtil.isNull(trainCarriage.getId())) {
            // 保存之前，先校验车厢是否存在
            DailyTrainCarriage trainCarriageDB = selectByUnique(req.getDate(), req.getTrainCode(), req.getIndex());

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
    public PageResp<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryReq request) {
        // 查询条件类
        DailyTrainCarriageExample carriageExample = new DailyTrainCarriageExample();
        // 设置按 'id' 降序排序
        carriageExample.setOrderByClause("date asc, train_code asc, `index` asc");

        DailyTrainCarriageExample.Criteria criteria = carriageExample.createCriteria();

        /**
         * 筛选时间
         */
        if (ObjectUtil.isNotEmpty(request.getDate())) {
            carriageExample.createCriteria().andDateEqualTo(request.getDate());
        }

        /**
         * 筛选车次
         */
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            carriageExample.createCriteria().andTrainCodeEqualTo(request.getTrainCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<DailyTrainCarriage> carriages = trainMapper.selectByExample(carriageExample);

        // 获取总条数
        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(carriages);

        // 封装成查询的响应值
        List<DailyTrainCarriageQueryResponse> queryResponses = BeanUtil.copyToList(carriages, DailyTrainCarriageQueryResponse.class);
        PageResp<DailyTrainCarriageQueryResponse> carriagePageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        carriagePageResp.setTotal(pageInfo.getTotal());
        carriagePageResp.setList(queryResponses);
        return carriagePageResp;
    }

    @Override
    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DailyTrainCarriageQueryResponse> queryAll() {
        DailyTrainCarriageExample trainExample = new DailyTrainCarriageExample();
        trainExample.setOrderByClause("name_pinyin asc");
        List<DailyTrainCarriage> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, DailyTrainCarriageQueryResponse.class);
    }

    @Override
    public List<DailyTrainCarriage> selectByTrainCode(String trainCode) {
        DailyTrainCarriageExample trainCarriageExample = new DailyTrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` asc");
        DailyTrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        List<DailyTrainCarriage> trainCarriages = trainMapper.selectByExample(trainCarriageExample);
        return trainCarriages;
    }

    @Override
    public DailyTrainCarriage selectByUnique(Date date, String trainCode, Integer index) {
        DailyTrainCarriageExample trainCarriageExample = new DailyTrainCarriageExample();
        trainCarriageExample.createCriteria()
                .andDateEqualTo(date)
                .andIndexEqualTo(index)
                .andTrainCodeEqualTo(trainCode);
        List<DailyTrainCarriage> trainCarriage = trainMapper.selectByExample(trainCarriageExample);
        if (CollUtil.isNotEmpty(trainCarriage)){
            return trainCarriage.get(0);
        } else {
            return null;
        }
    }
}
