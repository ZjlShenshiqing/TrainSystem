package com.train.generator.gen;

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
    static String toPath = "generator\\src\\main\\java\\com\\train\\generator\\gen\\";
    // 寻找 Mybatis Generator 的配置文件
    static String pomPath = "generator/pom.xml";
    static {
        new File(toPath).mkdirs();
    }

    public static void main(String[] args) throws IOException, TemplateException {

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
