package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.DailyTrain;
import com.zjl.train.business.entity.DailyTrainExample;
import com.zjl.train.business.mapper.DailyTrainMapper;
import com.zjl.train.business.request.DailyTrainQueryReq;
import com.zjl.train.business.request.DailyTrainSaveReq;
import com.zjl.train.business.resp.DailyTrainQueryResponse;
import com.zjl.train.business.service.DailyTrainService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日车次信息服务接口实现类
 * Created By Zhangjilin 2024/11/30
 */
@Service
public class DailyTrainServiceImpl implements DailyTrainService {

    @Autowired
    private DailyTrainMapper dailyTrainMapper;

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
}
