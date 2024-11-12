package com.zjl.train.member.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.common.context.LoginMemberContext;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import com.zjl.train.member.entity.Passenger;
import com.zjl.train.member.entity.PassengerExample;
import com.zjl.train.member.mapper.PassengerCustomizableMapper;
import com.zjl.train.member.mapper.PassengerMapper;
import com.zjl.train.member.req.PassengerQueryReq;
import com.zjl.train.member.req.PassengerSaveReq;
import com.zjl.train.member.response.PassengerQueryResponse;
import com.zjl.train.member.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 乘车人服务层实现类
 * Created By Zhangjilin 2024/11/08
 */
@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private PassengerCustomizableMapper passengerCustomizableMapper;

    @Override
    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);

        // 获取当前登录的用户 ID
        Long memberId = LoginMemberContext.getId();
        if (memberId == null) {
            throw new RuntimeException("用户未登录或登录已过期");
    }

        // 根据身份证号和 memberId 查询乘车人是否存在
        Passenger existingPassenger = passengerCustomizableMapper.selectByMemberIdAndIdCard(memberId, passenger.getIdCard());

        if (existingPassenger == null) {
            // 插入新的乘车人信息
            passenger.setMemberId(memberId);
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        } else {
            // 更新已有的乘车人信息
            existingPassenger.setMemberId(memberId);
            existingPassenger.setName(passenger.getName());
            existingPassenger.setIdCard(passenger.getIdCard());
            existingPassenger.setType(passenger.getType());
            existingPassenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(existingPassenger);
        }
    }

    @Override
    public PageResp<PassengerQueryResponse> queryList(PassengerQueryReq request) {
        // 查询条件类
        PassengerExample passengerExample = new PassengerExample();
        // 创造条件，会重复用到，所以需要提取出来
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(request.getMemberId())) {
            // 创建一个查询条件：where memberId...
            criteria.andMemberIdEqualTo(request.getMemberId());
        }
        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);

        // 获取总条数
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengers);

        // 封装成查询的响应值
        List<PassengerQueryResponse> queryResponses = BeanUtil.copyToList(passengers, PassengerQueryResponse.class);
        PageResp<PassengerQueryResponse> passengerPageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        passengerPageResp.setTotal(pageInfo.getTotal());
        passengerPageResp.setList(queryResponses);
        return passengerPageResp;
    }
}
