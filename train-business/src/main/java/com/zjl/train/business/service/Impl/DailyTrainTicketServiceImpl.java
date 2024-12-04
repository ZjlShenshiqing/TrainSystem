package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.zjl.train.business.entity.DailyTrainTicket;
import com.zjl.train.business.entity.DailyTrainTicketExample;
import com.zjl.train.business.entity.Train;
import com.zjl.train.business.mapper.DailyTrainTicketMapper;
import com.zjl.train.business.request.DailyTrainSaveReq;
import com.zjl.train.business.request.DailyTrainTicketQueryReq;
import com.zjl.train.business.request.DailyTrainTicketSaveReq;
import com.zjl.train.business.resp.DailyTrainTicketQueryResponse;
import com.zjl.train.business.service.DailyTrainTicketService;
import com.zjl.train.business.service.TrainSeatService;
import com.zjl.train.business.service.TrainStationService;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 余票查询服务实现类 - 管理端
 * Created By Zhangjilin 2024/12/4
 */
@Service
public class DailyTrainTicketServiceImpl implements DailyTrainTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Autowired
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Autowired
    private TrainStationService trainStationService;

    @Autowired
    private TrainSeatService trainSeatService;

    @Override
    public void save(DailyTrainTicketSaveReq request) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(request, DailyTrainTicket.class);
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }
    }

    @Override
    public PageResp<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryReq request) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        if (ObjUtil.isNotNull(request.getDate())) {
            criteria.andDateEqualTo(request.getDate());
        }
        if (ObjUtil.isNotEmpty(request.getTrainCode())) {
            criteria.andTrainCodeEqualTo(request.getTrainCode());
        }
        if (ObjUtil.isNotEmpty(request.getStart())) {
            criteria.andStartEqualTo(request.getStart());
        }
        if (ObjUtil.isNotEmpty(request.getEnd())) {
            criteria.andEndEqualTo(request.getEnd());
        }

        LOG.info("查询页码：{}", request.getPage());
        LOG.info("每页条数：{}", request.getSize());
        PageHelper.startPage(request.getPage(), request.getSize());
        // TODO
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        return null;
    }

    @Override
    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }

    // TODO
    @Override
    public void genDailyTicket(Date date, Train train) {

    }
}
