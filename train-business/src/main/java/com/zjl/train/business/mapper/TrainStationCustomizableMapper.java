package com.zjl.train.business.mapper;

import com.zjl.train.business.entity.TrainStation;
import org.apache.ibatis.annotations.Param;

/**
 * 车次经停站信息自定义持久层
 * Created By Zhangjilin 2024/11/23
 */
public interface TrainStationCustomizableMapper {

    TrainStation selectByUnique(@Param("name") String name);
}
