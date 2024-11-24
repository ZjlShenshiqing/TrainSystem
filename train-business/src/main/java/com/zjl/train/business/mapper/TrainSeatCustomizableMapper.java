package com.zjl.train.business.mapper;

import com.zjl.train.business.entity.TrainSeat;
import org.apache.ibatis.annotations.Param;

/**
 * 车次座位信息自定义持久层
 * Created By Zhangjilin 2024/11/24
 */
public interface TrainSeatCustomizableMapper {

    TrainSeat selectByUnique(@Param("name") String name);
}
