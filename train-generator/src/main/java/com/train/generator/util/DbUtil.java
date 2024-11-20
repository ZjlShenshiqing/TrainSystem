package com.train.generator.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从数据库中读取字段信息的工具类
 *
 * Created By Zhangjilin 2024/11/19
 */
public class DbUtil {

    public static String url = "";
    public static String user = "";
    public static String password = "";

    /**
     * 获取数据库连接信息
     * Created By Zhangjilin 2024/11/19
     * @return 连接信息
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = DbUtil.url;
            String user = DbUtil.user;
            String password = DbUtil.password;
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获得表注释 comment
     * Created By Zhangjilin 2024/11/19
     * @param tableName
     * @return
     * @throws Exception
     */
    public static String getTableComment(String tableName) throws Exception {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select table_comment from information_schema.tables Where table_name = '" + tableName + "'");
        String tableNameCH = "";
        if (rs != null) {
            while(rs.next()) {
                tableNameCH = rs.getString("table_comment");
                break;
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("表名：" + tableNameCH);
        return tableNameCH;
    }

    /**
     * 获得所有列信息
     *
     * 流程：
     * 连接到数据库，执行 SHOW FULL COLUMNS 查询表字段。
     * 遍历查询结果，提取字段名、类型、注释等信息。
     * 格式化字段信息：
     * 转换为驼峰命名。
     * 转换数据库类型为 Java 类型。
     * 提取注释中的中文名和枚举信息。
     * 提取字段长度。
     * 将所有字段封装到 Field 对象中，返回字段列表。
     *
     * Created By Zhangjilin 2024/11/19
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<Field> getColumnByTableName(String tableName) throws Exception {
        List<Field> fieldList = new ArrayList<>();
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("show full columns from `" + tableName + "`");
        if (rs != null) {
            while(rs.next()) {
                String columnName = rs.getString("Field");
                String type = rs.getString("Type");
                String comment = rs.getString("Comment");
                String nullAble = rs.getString("Null"); //YES NO
                Field field = new Field();
                field.setName(columnName);
                field.setNameHump(lineToHump(columnName));
                field.setNameBigHump(lineToBigHump(columnName));
                field.setType(type);
                field.setJavaType(DbUtil.sqlTypeToJavaType(rs.getString("Type")));
                field.setComment(comment);
                if (comment.contains("|")) {
                    field.setNameChinese(comment.substring(0, comment.indexOf("|")));
                } else {
                    field.setNameChinese(comment);
                }
                field.setNullable("YES".equals(nullAble));
                if (type.toUpperCase().contains("varchar".toUpperCase())) {
                    String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
                    field.setLength(Integer.valueOf(lengthStr));
                } else {
                    field.setLength(0);
                }
                if (comment.contains("枚举")) {
                    field.setEnums(true);

                    // 提取枚举信息（如果字段注释包含“枚举”关键字）
                    int start = comment.indexOf("[");
                    int end = comment.indexOf("]");
                    String enumsName = comment.substring(start + 1, end); // CourseLevelEnum
                    String enumsConst = StrUtil.toUnderlineCase(enumsName)
                            .toUpperCase().replace("_ENUM", "");
                    field.setEnumsConst(enumsConst);
                } else {
                    field.setEnums(false);
                }
                fieldList.add(field);
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("列信息：" + JSONUtil.toJsonPrettyStr(fieldList));
        return fieldList;
    }

    /**
     * 下划线转小驼峰：member_id 转成 memberId
     *
     * Created By Zhangjilin 2024/11/19
     */
    public static String lineToHump(String str){
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转大驼峰：member_id 转成 MemberId
     *
     * Created By Zhangjilin 2024/11/19
     */
    public static String lineToBigHump(String str){
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 数据库类型转为Java类型
     *
     * Created By Zhangjilin 2024/11/19
     */
    public static String sqlTypeToJavaType(String sqlType) {
        if (sqlType.toUpperCase().contains("varchar".toUpperCase())
                || sqlType.toUpperCase().contains("char".toUpperCase())
                || sqlType.toUpperCase().contains("text".toUpperCase())) {
            return "String";
        } else if (sqlType.toUpperCase().contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("time".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("date".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("bigint".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.toUpperCase().contains("long".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("decimal".toUpperCase())) {
            return "BigDecimal";
        } else if (sqlType.toUpperCase().contains("boolean".toUpperCase())) {
            return "Boolean";
        } else {
            return "String";
        }
    }
}
