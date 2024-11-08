package com.zjl.train.member.service;

import com.zjl.train.member.req.PassengerSaveReq;
import org.springframework.stereotype.Service;

/**
 * 乘客服务层接口
 * Created By Zhangjilin 2024/11/08
 */
@Service
public interface PassengerService {

    /**
     * Created By Zhangjilin 2024/11/08
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(PassengerSaveReq request);
}
