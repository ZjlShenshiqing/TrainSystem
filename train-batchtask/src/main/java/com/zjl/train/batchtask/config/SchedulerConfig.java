package com.zjl.train.batchtask.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 定时调度模块配置类
 * Created By Zhangjilin 2024/11/28
 */
@Configuration
public class SchedulerConfig {

    @Resource
    private MyJobFactory myJobFactory;

    @Bean
    // dataSource是我们在application.yml配置好的数据源
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);        // 配置 Quartz 使用的数据源
        factory.setJobFactory(myJobFactory);      // 设置自定义的 JobFactory
        factory.setStartupDelay(2);               // 设置调度器启动延迟时间
        return factory;
    }
}
