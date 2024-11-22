package com.zjl.train.business.mapper;

import com.zjl.train.business.entity.Train;
import org.apache.ibatis.annotations.Param;

/**
 * 车次自定义持久层
 * Created By Zhangjilin 2024/11/22
 */
public interface TrainCustomizableMapper {

    Train selectByUnique(@Param("name") String name);
}
