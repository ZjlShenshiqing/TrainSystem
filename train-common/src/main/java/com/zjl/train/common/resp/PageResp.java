package com.zjl.train.common.resp;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询返回结果，会返回总条数和列表
 * Created By Zhangjilin 2024/11/12
 */
public class PageResp<T> implements Serializable {

    // 总条数
    private Long total;

    // 当前页的列表
    private List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


}
