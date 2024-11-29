package com.zjl.train.batchtask.request;

/**
 * 存储和传递定时任务的相关信息
 * Created By Zhangjilin 2024/11/29
 *
 * 描述：
 * CronJobReq 类通常用于接收或传递与 Quartz 定时任务相关的配置信息，例如：
 *
 * group：表示任务所属的组，Quartz 中也有任务组的概念，任务可以根据组进行组织和管理。
 * name：表示任务的名称，Quartz 任务也需要指定一个名称，以便在调度器中标识该任务。
 * description：任务的描述信息，Quartz 任务也支持描述字段，用于对任务进行简短的说明。
 * cronExpression：Quartz 中用于定义任务调度规则的 Cron 表达式，CronJobReq 类的这个字段就是用来存储 Cron 表达式的。
 */
public class CronJobReq {
    private String group;

    private String name;

    private String description;

    private String cronExpression;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CronJobDto{");
        sb.append("cronExpression='").append(cronExpression).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
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
}
