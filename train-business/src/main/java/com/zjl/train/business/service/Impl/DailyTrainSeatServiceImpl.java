package com.zjl.train.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.business.entity.DailyTrainCarriage;
import com.zjl.train.business.entity.DailyTrainSeat;
import com.zjl.train.business.entity.DailyTrainSeatExample;
import com.zjl.train.business.enums.SeatColEnum;
import com.zjl.train.business.mapper.DailyTrainSeatMapper;
import com.zjl.train.business.request.DailyTrainSeatQueryReq;
import com.zjl.train.business.request.DailyTrainSeatSaveReq;
import com.zjl.train.business.resp.DailyTrainSeatQueryResponse;
import com.zjl.train.business.service.DailyTrainSeatService;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 每日车次座位信息服务接口实现类
 * Created By Zhangjilin 2024/12/2
 */
@Service
public class DailyTrainSeatServiceImpl implements DailyTrainSeatService {

    @Autowired
    private DailyTrainSeatMapper trainMapper;

    /**
     * 方案已弃用，该成由车次自动生成
     * Created By Zhangjilin 2024/11/28
     * @param req
     */
    @Override
    @Deprecated
    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat trainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        if (ObjectUtil.isNull(trainSeat.getId())) {
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainMapper.insert(trainSeat);
        } else {
            trainSeat.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(trainSeat);
        }
    }


    @Override
    public PageResp<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryReq request) {
        // 查询条件类
        DailyTrainSeatExample dailySeatExample = new DailyTrainSeatExample();
        // 设置按 'id' 降序排序
        dailySeatExample.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");

        DailyTrainSeatExample.Criteria criteria = dailySeatExample.createCriteria();
        // 判断是否有车次信息
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            criteria.andTrainCodeEqualTo(request.getTrainCode());
        }

        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<DailyTrainSeat> dailySeats = trainMapper.selectByExample(dailySeatExample);

        // 获取总条数
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailySeats);

        // 封装成查询的响应值
        List<DailyTrainSeatQueryResponse> queryResponses = BeanUtil.copyToList(dailySeats, DailyTrainSeatQueryResponse.class);
        PageResp<DailyTrainSeatQueryResponse> dailySeatPageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        dailySeatPageResp.setTotal(pageInfo.getTotal());
        dailySeatPageResp.setList(queryResponses);
        return dailySeatPageResp;
    }

    @Override
    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DailyTrainSeatQueryResponse> queryAll() {
        DailyTrainSeatExample trainExample = new DailyTrainSeatExample();
        trainExample.setOrderByClause("name_pinyin asc");
        List<DailyTrainSeat> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, DailyTrainSeatQueryResponse.class);
    }


    @Override
    @Transactional
    public void autoTrainSeat(String trainCode) {
        // 先清空数据库的座位信息，再生成座位
        DailyTrainSeatExample trainSeatExample = new DailyTrainSeatExample();
        DailyTrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        trainMapper.deleteByExample(trainSeatExample);

        // 查询当前车次下的所有车厢 TODO
        List<DailyTrainCarriage> trainCarriages = null;

        // 循环生成每个车厢的座位
        for (DailyTrainCarriage trainCarriage : trainCarriages) {
            // 拿到车厢数据：行数，座位类型（一等座or二等座）
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();

            // 每个座位的唯一索引
            int seatIndex = 1;

            // 根据车厢的座位类型来进行筛选 一等座只有ACDF可以选择，二等座有ABCDF可以选择，根据类型来自动填充列
            List<SeatColEnum> seatColumnType = SeatColEnum.getColsByType(seatType);

            // 循环行数：每一排
            for (int row = 1; row <= rowCount; row++) {
                // 循环列数：每个座位
                for (SeatColEnum seatColEnum : seatColumnType) {
                    DailyTrainSeat trainSeat = new DailyTrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2)); // 是两位，不填充，不是两位填充0
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(DateTime.now());
                    trainSeat.setUpdateTime(DateTime.now());
                    trainMapper.insert(trainSeat);
                }
            }
        }
    }

    @Override
    public List<DailyTrainCarriage> selectByTrainCode(String trainCode) {
        return List.of();
    }
}
