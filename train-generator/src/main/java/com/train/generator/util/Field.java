package com.train.generator.util;

/**
 * 描述数据库表字段信息的类
 *
 * Created By Zhangjilin 2024/11/19
 */
public class Field {
    // 假设现在有字段名 course_id
    private String name; // 字段名
    private String nameHump; // 字段名小驼峰 courseId
    private String nameBigHump; // 字段名大驼峰 CourseId
    private String nameChinese; // 字段中文名
    private String type; // 字段类型
    private String javaType; // 字段的java类型
    private String comment; // 字段注释
    private Boolean nullable; // 是否可为空
    private Integer length; // 字段长度
    private Boolean enums; // 是否是枚举
    private String enumsConst; // 枚举常量

    public Field(String name, String nameHump, String nameBigHump, String nameChinese, String type, String javaType, String comment, Boolean nullable, Integer length, Boolean enums, String enumsConst) {
        this.name = name;
        this.nameHump = nameHump;
        this.nameBigHump = nameBigHump;
        this.nameChinese = nameChinese;
        this.type = type;
        this.javaType = javaType;
        this.comment = comment;
        this.nullable = nullable;
        this.length = length;
        this.enums = enums;
        this.enumsConst = enumsConst;
    }

    public Field() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHump() {
        return nameHump;
    }

    public void setNameHump(String nameHump) {
        this.nameHump = nameHump;
    }

    public String getNameBigHump() {
        return nameBigHump;
    }

    public void setNameBigHump(String nameBigHump) {
        this.nameBigHump = nameBigHump;
    }

    public String getNameChinese() {
        return nameChinese;
    }

    public void setNameChinese(String nameChinese) {
        this.nameChinese = nameChinese;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getEnums() {
        return enums;
    }

    public void setEnums(Boolean enums) {
        this.enums = enums;
    }

    public String getEnumsConst() {
        return enumsConst;
    }

    public void setEnumsConst(String enumsConst) {
        this.enumsConst = enumsConst;
    }
}
