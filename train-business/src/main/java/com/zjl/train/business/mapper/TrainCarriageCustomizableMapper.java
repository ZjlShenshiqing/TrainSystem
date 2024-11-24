package com.zjl.train.business.mapper;

import com.zjl.train.business.entity.TrainCarriage;
import org.apache.ibatis.annotations.Param;

/**
 * 车次车厢信息自定义持久层
 * Created By Zhangjilin 2024/11/24
 */
public interface TrainCarriageCustomizableMapper {

    TrainCarriage selectByUnique(@Param("name") String name);
}
