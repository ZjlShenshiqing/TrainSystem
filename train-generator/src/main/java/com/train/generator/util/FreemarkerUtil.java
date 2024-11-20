package com.train.generator.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Freemarker 工具类
 *
 * Created By Zhangjilin 2024/11/17
 *
 * 它主要实现了两大功能：
 * 1、初始化 Freemarker 的模板配置。
 * 2、根据模板和数据生成文件。
 *
 * Template 是 Freemarker 的核心类，用于表示加载后的模板。
 * 它会从指定的模板文件中加载内容，并在运行时结合动态数据生成输出文件。
 */
public class FreemarkerUtil {

    // 模板文件的存放位置
    static String ftlPath = "generator\\src\\main\\java\\com\\train\\generator\\ftl\\";

    // 加载后的模板文件
    static Template template;

    /**
     * 初始化 Freemarker 配置
     * Created By Zhangjilin 2024/11/17
     *
     * @throws IOException Configuration configuration = new Configuration(Configuration.VERSION_2_3_31)
     * 创建一个 Freemarker 配置对象。
     * Configuration.VERSION_2_3_31 指定 Freemarker 的版本。
     *
     *
     * configuration.setDirectoryForTemplateLoading(new File(ftlPath))
     * 设置模板文件的根目录，告诉 Freemarker 从哪里加载模板文件。
     * new File(ftlPath) 会将 ftlPath 转换为文件路径。
     *
     *
     * configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31))
     * 设置对象包装器，告诉 Freemarker 如何处理 Java 对象。
     * 这里使用了默认的对象包装器。
     */
    public static void initConfiguration(String ftlName) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        // 指定模板文件的根目录
        configuration.setDirectoryForTemplateLoading(new File(ftlPath));
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
        // 加载指定的模板文件
        template = configuration.getTemplate(ftlName);
    }

    /**
     * 文件生成方法
     * Created By Zhangjilin 2024/11/17
     *
     * @param filename
     * @param map
     * @throws IOException
     * @throws TemplateException
     *
     * 这是一个静态方法，用于根据 Freemarker 模板和动态数据生成文件。
     *
     * 这段代码的目的是 读取模板文件（由 template 变量指定），
     * 结合动态数据 map，将生成的内容写入到指定的文件中 filename。
     */
    public static void generator(String filename, Map<String, Object> map) throws IOException, TemplateException {
        FileWriter fileWriter = new FileWriter(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        template.process(map, bufferedWriter);
        bufferedWriter.flush();
        fileWriter.close();
    }
}


