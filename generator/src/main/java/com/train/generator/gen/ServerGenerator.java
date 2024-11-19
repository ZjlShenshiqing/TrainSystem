package com.train.generator.gen;

import com.train.generator.util.DbUtil;
import com.train.generator.util.Field;
import com.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public static void main(String[] args) throws Exception {
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

        // 为DBUtil设置数据源
        Node connectionUrl = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        DbUtil.url = connectionUrl.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();

        String Domain = domainObjectName.getText();

        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // a_b -> a-b
        String do_main = tableName.getText().replaceAll("_", "-");

        // 获取表的中文名
        String tableChineseName = DbUtil.getTableComment(tableName.getText());
        System.out.println("表名：" + tableChineseName);

        // 获取表的字段信息
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());
        // 通过表字段的信息，获取所有字段的java类型
        Set<String> javaTypesSet = getJavaTypes(fieldList);


        // 组装参数
        HashMap<String, Object> param = new HashMap<>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("nameCn", tableChineseName);
        param.put("fieldList", fieldList);
        param.put("typeSet", javaTypesSet);

        // 通过模板生成实体类
        // generator(Domain, param, "service");
        // generator(Domain, param, "controller");
        generator(Domain, param, "request", "saveReq");
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
    private static void generator(String Domain, HashMap<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        // 加载模板
        FreemarkerUtil.initConfiguration(target + ".ftl");

        // 包的路径
        String toPath = serverPath + packageName + "/";
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

    /**
     * 获取数据库一张表的所有字段中的所有的Java类型，使用Set来去重
     * Created By Zhangjilin 2024/11/19
     *
     *
     * 先看一下一个fieldList里面的field
     *     {
     *         "name": "name",
     *         "nameHump": "name",
     *         "nameBigHump": "Name",
     *         "nameChinese": "姓名",
     *         "type": "varchar(20)",
     *         "javaType": "String", 这个是需要去获取的字段
     *         "comment": "姓名",
     *         "nullable": false,
     *         "length": 20,
     *         "enums": false
     *     }
     *
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
