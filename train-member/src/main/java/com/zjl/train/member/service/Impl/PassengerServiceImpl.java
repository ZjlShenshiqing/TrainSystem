package com.zjl.train.member.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.zjl.train.common.context.LoginMemberContext;
import com.zjl.train.common.util.SnowUtil;
import com.zjl.train.member.entity.Passenger;
import com.zjl.train.member.mapper.PassengerMapper;
import com.zjl.train.member.req.PassengerSaveReq;
import com.zjl.train.member.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 乘车人服务层实现类
 * Created By Zhangjilin 2024/11/08
 */
@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    @Override
    public void save(PassengerSaveReq request) {
        DateTime now = DateTime.now();
        // 将请求信息拷贝成实体类
        Passenger passenger = BeanUtil.copyProperties(request, Passenger.class);
        passenger.setMemberId(LoginMemberContext.getId());
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}
