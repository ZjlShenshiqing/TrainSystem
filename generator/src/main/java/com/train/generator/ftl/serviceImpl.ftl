package com.zjl.train.member.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjl.train.common.context.LoginMemberContext;
import com.zjl.train.common.resp.PageResp;
import com.zjl.train.common.util.SnowUtil;
import com.zjl.train.member.entity.${Domain};
import com.zjl.train.member.entity.${Domain}Example;
import com.zjl.train.member.mapper.${Domain}CustomizableMapper;
import com.zjl.train.member.mapper.${Domain}Mapper;
import com.zjl.train.member.request.${Domain}QueryReq;
import com.zjl.train.member.request.${Domain}SaveReq;
import com.zjl.train.member.response.${Domain}QueryResponse;
import com.zjl.train.member.service.${Domain}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 乘车人服务层实现类
 * Created By Zhangjilin 2024/11/08
 */
@Service
public class ${Domain}ServiceImpl implements ${Domain}Service {

    @Autowired
    private ${Domain}Mapper ${domain}Mapper;

    @Autowired
    private ${Domain}CustomizableMapper ${domain}CustomizableMapper;

    @Override
    public void save(${Domain}SaveReq req) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);

        // 这里判断的是是新增还是编辑
        if (ObjectUtil.isNotNull(${domain}.getId())) {
            // 设置更新时间，并且更新前端传过来的${domain}进入数据库
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKey(${domain});
        }

        // 获取当前登录的用户 ID
        Long memberId = LoginMemberContext.getId();
        if (memberId == null) {
            throw new RuntimeException("用户未登录或登录已过期");
        }

        // 根据身份证号和 memberId 查询乘车人是否存在
        ${Domain} existing${Domain} = ${domain}CustomizableMapper.selectByMemberIdAndIdCard(memberId, ${domain}.getIdCard());

        if (existing${Domain} == null) {
            // 插入新的乘车人信息
            ${domain}.setMemberId(memberId);
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
        } else {
            // 更新已有的乘车人信息
            existing${Domain}.setMemberId(memberId);
            existing${Domain}.setName(${domain}.getName());
            existing${Domain}.setIdCard(${domain}.getIdCard());
            existing${Domain}.setType(${domain}.getType());
            existing${Domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKey(existing${Domain});
        }
    }

    @Override
    public PageResp<${Domain}QueryResponse> queryList(${Domain}QueryReq request) {
        // 查询条件类
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        // 设置按 'id' 降序排序
        ${domain}Example.setOrderByClause("id desc");
        // 创造条件，会重复用到，所以需要提取出来
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();
        if (ObjectUtil.isNotNull(request.getMemberId())) {
            // 创建一个查询条件：where memberId...
            criteria.andMemberIdEqualTo(request.getMemberId());
        }
        // 分页：参数1：查第几页 ，参数2：查第几条
        PageHelper.startPage(request.getPage(),request.getSize());
        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain}Example);

        // 获取总条数
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}s);

        // 封装成查询的响应值
        List<${Domain}QueryResponse> queryResponses = BeanUtil.copyToList(${domain}s, ${Domain}QueryResponse.class);
        PageResp<${Domain}QueryResponse> ${domain}PageResp = new PageResp<>();

        // 将查询的响应值和总条数封装成分页的响应值
        ${domain}PageResp.setTotal(pageInfo.getTotal());
        ${domain}PageResp.setList(queryResponses);
        return ${domain}PageResp;
    }

    @Override
    public void delete(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}