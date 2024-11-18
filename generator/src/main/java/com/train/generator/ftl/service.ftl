package com.zjl.train.member.service;

import com.zjl.train.common.resp.PageResp;
import com.zjl.train.member.request.${Domain}QueryReq;
import com.zjl.train.member.request.${Domain}SaveReq;
import com.zjl.train.member.response.${Domain}QueryResponse;
import org.springframework.stereotype.Service;

/**
 * 乘客服务层接口
 * Created By Zhangjilin 2024/11/08
 */
@Service
public interface ${Domain}Service {

    /**
     * Created By Zhangjilin 2024/11/08
     * 保存操作，不需要返回值，因为保存后界面会刷新列表，不需要返回保存成功后的数据
     */
    public void save(${Domain}SaveReq request);

    /**
     * Created By Zhangjilin 2024/11/11
     * 查询列表：查询乘车人列表
     */
    public PageResp<${Domain}QueryResponse> queryList(${Domain}QueryReq request);

    /**
     * Created By Zhangjilin 2024/11/16
     * 根据id删除乘车人信息
     * @param id
     */
    public void delete(Long id);
}