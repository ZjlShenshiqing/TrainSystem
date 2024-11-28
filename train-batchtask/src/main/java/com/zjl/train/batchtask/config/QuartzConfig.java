package com.zjl.train.batchtask.config;

import com.zjl.train.batchtask.job.TestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时调度模块Quartz配置类
 * Created By Zhangjilin 2024/11/28
 */
@Configuration
public class QuartzConfig {
     /**
      * 声明一个任务
      * @return
      */
     @Bean
     public JobDetail jobDetail() {
         return JobBuilder.newJob(TestJob.class)
                 .withIdentity("TestJob", "test")
                 .storeDurably()
                 .build();
     }

     /**
      * 声明一个触发器，什么时候触发这个任务
      * @return
      */
     @Bean
     public Trigger trigger() {
         return TriggerBuilder.newTrigger()
                 .forJob(jobDetail())
                 .withIdentity("trigger", "trigger")
                 .startNow()
                 .withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ?"))
                 .build();
     }
}
