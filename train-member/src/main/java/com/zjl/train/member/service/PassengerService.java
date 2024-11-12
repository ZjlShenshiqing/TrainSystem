package com.zjl.train.member.service;

import com.zjl.train.member.req.PassengerQueryReq;
import com.zjl.train.member.req.PassengerSaveReq;
import com.zjl.train.member.response.PassengerQueryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Created By Zhangjilin 2024/11/11
     * 查询列表：查询乘车人列表
     */
    public List<PassengerQueryResponse> queryList(PassengerQueryReq request);
}
