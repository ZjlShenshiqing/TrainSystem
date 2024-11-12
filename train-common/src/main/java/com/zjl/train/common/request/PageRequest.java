package com.zjl.train.common.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

/**
 * 分页查询类，其他查询请求需要分页的继承这个类
 * Created By Zhangjilin 2024/11/12
 */
public class PageRequest {
    @NotNull(message = "[页码] 不能为空！")
    private Integer page;

    // 限制条数，防止后端爆炸
    @NotNull(message = "[每页条数] 不能为空")
    @Max(value = 100, message = "[每页条数] 不能超过100")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
