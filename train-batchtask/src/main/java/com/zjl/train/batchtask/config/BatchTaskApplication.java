package com.zjl.train.batchtask.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * 定时调度模块启动类
 * Created By Zhangjilin 2024/11/28
 */
@SpringBootApplication
@ComponentScan("com.zjl")
@MapperScan("com.zjl.train.*.mapper")
public class BatchTaskApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BatchTaskApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchTaskApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！");
        LOG.info("测试地址：\thttp://127.0.0.1:{}{}/hello",env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
    }
}