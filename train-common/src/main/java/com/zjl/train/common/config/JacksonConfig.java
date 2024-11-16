package com.zjl.train.common.config;

import org.springframework.context.annotation.Configuration;

/**
 * 解决前后端交互Long类型精度丢失的问题
 * Created By Zhangjilin 2024/11/16
 *
 *
 * Jackson 是一种用于 Java 对象和 JSON 数据相互转换的库，
 * 它在 Spring Boot 中被广泛用于处理 API 的 JSON 请求和响应。
 *
 *
 * ObjectMapper 会把 Java 中的 Long 类型数据直接序列化为 JSON 中的数字格式。
 * 但由于 JavaScript 对于长整型 (Long) 数据的处理会丢失精度，所以在序列化时，
 * 通常需要将 Long 类型转换为字符串 (String) 格式，避免前端解析错误。
 *
 *
 * Jackson2ObjectMapperBuilder：这是 Spring 提供的工具类，用于方便地创建和配置 ObjectMapper。
 *
 *
 * 将 Java 中的 Long 类型配置为使用 ToStringSerializer 进行序列化。
 * ToStringSerializer.instance 是 Jackson 提供的一个序列化器，它会将数据转换为字符串格式。
 */
@Configuration
public class JacksonConfig {
    /**
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 构建并返回一个 ObjectMapper 对象, 不创建XML解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 注册自定义的序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
    **/
}
