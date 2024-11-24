package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.Station;
import com.zjl.train.business.entity.StationExample;
import com.zjl.train.business.mapper.StationCustomizableMapper;
import com.zjl.train.business.mapper.StationMapper;
import com.zjl.train.business.request.StationQueryReq;
import com.zjl.train.business.request.StationSaveReq;
import com.zjl.train.business.resp.StationQueryResponse;
import com.zjl.train.business.service.StationService;
import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.exception.BusinessExceptionEnum;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车站信息服务接口实现类
 * Created By Zhangjilin 2024/11/20
 */
@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private StationCustomizableMapper stationCustomizableMapper;

    @Override
    public void save(StationSaveReq req) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);
        if (ObjectUtil.isNull(station.getId())) {
            // 保存之前，先校验唯一键是否存在
            Station stationDB = stationCustomizableMapper.selectByUnique(req.getName());

            // 首先判断是否已经有同名的车站
            if (ObjectUtil.isNotEmpty(stationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            // 开始保存的操作，将信息插入到数据库中
            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        } else {
            // 更新操作
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKey(station);
        }
    }


    @Override
    public PageResp<StationQueryResponse> queryList(StationQueryReq request) {
        // 查询条件类
        StationExample passengerExample = new StationExample();
        // 设置按 'id' 降序排序
        passengerExample.setOrderByClause("id desc");

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<Station> passengers = stationMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<Station> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<StationQueryResponse> queryResponses = BeanUtil.copyToList(passengers, StationQueryResponse.class);
        PageResp<StationQueryResponse> passengerPageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        passengerPageResp.setTotal(pageInfo.getTotal());
        passengerPageResp.setList(queryResponses);
        return passengerPageResp;
    }

    @Override
    public void delete(Long id) {
        stationMapper.deleteByPrimaryKey(id);
    }


    @Override
    public List<StationQueryResponse> queryAll() {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_py asc");
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        return BeanUtil.copyToList(stationList, StationQueryResponse.class);
    }
}
