package com.train.generator.gen;

import com.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过 Freemarker 生成代码
 * Created By Zhangjilin 2024/11/17
 */
public class ServerGenerator {
    static String serverPath = "train-[module]/src/main/java/com/zjl/train/[module]/";
    // 寻找 Mybatis Generator 的配置文件
    static String pomPath = "generator/pom.xml";
    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws IOException, TemplateException, DocumentException {
        // Generator 的 xml 文件
        // 比如：src/main/resources/generator-config-member.xml
        String generatorPath = getGeneratorPath();

        // 获取module名字 将前后删除，只留下模块名
        String module = generatorPath.replace("src/main/resources/generator-config-", "")
                     .replace(".xml","");

        serverPath = serverPath.replace("[module]", module);

        // 读取 Generator 的 xml 文件 的 table 节点
        Document document = new SAXReader().read("generator/" + generatorPath); // 读取xml文件
        Node table = document.selectSingleNode("//table"); // 读取table节点
        System.out.println(table);
        // <table tableName="passenger" domainObjectName="Passenger"/>
        // 表名
        Node tableName = table.selectSingleNode("@tableName"); // 通过table节点去查属性 passenger
        // 实体类名称
        Node domainObjectName = table.selectSingleNode("@domainObjectName"); // Passenger

        String Domain = domainObjectName.getText();

        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // a_b -> a-b
        String do_main = tableName.getText().replaceAll("_", "-");

        // 组装参数
        HashMap<String, Object> param = new HashMap<>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);

        // 通过模板生成实体类
        generator(Domain, param, "service");
        generator(Domain, param, "controller");
    }

    /**
     * 通过模板生成代码
     *
     * Created By Zhangjilin 2024/11/19
     * @param Domain
     * @param param
     * @param target 生成的是哪个层的代码
     * @throws IOException
     * @throws TemplateException
     */
    private static void generator(String Domain, HashMap<String, Object> param, String target) throws IOException, TemplateException {
        // 加载模板
        FreemarkerUtil.initConfiguration(target + ".ftl");

        // 包的路径
        String toPath = serverPath + target + "/";
        new File(toPath).mkdirs();

        // 将小写的改成大写的的
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);

        // 生成的代码的路径
        String fileName = toPath + Domain + Target + ".java";

        // 通过上面这些东西生成代码
        FreemarkerUtil.generator(fileName, param);
    }

    /**
     * 从 Maven 项目的 pom.xml 文件中读取 Generator 的文件路径
     * Created By Zhangjilin 2024/11/17
     * @return
     * @throws DocumentException
     */
    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        // 配置 Maven 的 XML 命名空间
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        /**
         * 查找指定节点
         * //：表示从 XML 的根节点开始查找。
         * pom:configurationFile：表示命名空间前缀为 pom，节点名称为 configurationFile。
         */
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }
}
