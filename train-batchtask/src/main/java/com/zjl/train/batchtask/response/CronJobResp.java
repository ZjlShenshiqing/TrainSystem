package com.zjl.train.batchtask.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * 将定时任务的相关信息从服务器返回给客户端
 * Created By Zhangjilin 2024/11/29
 *
 * 字段解释
 * group：任务所属的组。Quartz 允许将任务按组进行组织，以便管理任务时可以根据组来查找和操作任务。
 *
 * name：任务的名称。每个任务在 Quartz 中都有唯一的名称，用来标识任务。
 *
 * description：任务的描述，提供一些关于该任务的额外信息。比如任务的功能或作用。
 *
 * state：任务的当前状态。这个字段用来表示任务是否正在运行、暂停或者停止等。通常是通过枚举值或状态字符串来表示任务的不同状态。
 *
 * cronExpression：任务的 Cron 表达式，指定任务执行的频率和时间。Cron 表达式是一种常用的定时任务调度方式，用于指定任务的执行规则（例如每天、每小时、每分钟等）。
 *
 * nextFireTime：任务下次触发的时间，表示任务下一次执行的具体时间。
 *
 * preFireTime：任务上次触发的时间，表示任务最后一次执行的时间。
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // 在将该类的对象序列化为 JSON 时，只包含非空字段
public class CronJobResp {
    private String group;

    private String name;

    private String description;

    private String state;

    private String cronExpression;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date nextFireTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date preFireTime;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CronJobDto{");
        sb.append("cronExpression='").append(cronExpression).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", nextFireTime=").append(nextFireTime);
        sb.append(", preFireTime=").append(preFireTime);
        sb.append('}');
        return sb.toString();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPreFireTime() {
        return preFireTime;
    }

    public void setPreFireTime(Date preFireTime) {
        this.preFireTime = preFireTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
