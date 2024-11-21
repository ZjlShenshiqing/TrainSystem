package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.zjl.train.business.entity.Station;
import com.zjl.train.business.entity.StationExample;
import com.zjl.train.business.mapper.StationMapper;
import com.zjl.train.business.request.StationQueryReq;
import com.zjl.train.business.request.StationSaveReq;
import com.zjl.train.business.resp.StationQueryResponse;
import com.zjl.train.business.service.StationService;
import com.zjl.train.common.resp.PageResp;
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

    @Override
    public void save(StationSaveReq request) {

    }

    @Override
    public PageResp<StationQueryResponse> queryList(StationQueryReq request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<StationQueryResponse> queryAll() {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_pinyin asc");
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        return BeanUtil.copyToList(stationList, StationQueryResponse.class);
    }
}
