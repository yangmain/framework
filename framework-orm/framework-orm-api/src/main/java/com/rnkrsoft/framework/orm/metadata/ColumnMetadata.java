package com.rnkrsoft.framework.orm.metadata;

import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.ValueMode;
import lombok.*;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class ColumnMetadata {
    /**
     * 是否为物理主键字段
     */
    boolean primaryKey = false;
    /**
     * 所属表元信息
     */
    TableMetadata tableMetadata;
    /**
     * 实体类对象
     */
    Class entityClass;
    /**
     * 字段对象
     */
    Field columnField;
    /**
     * Java字段名称
     */
    String javaName = "";
    /**
     * Java数据类型
     */
    Class javaType;
    /**
     * 数据库字段名
     */
    String jdbcName = "";
    /**
     * 数据库数据类型 包含有长度信息或者精确度信息
     */
    String fullJdbcType;
    /**
     * 字符长度
     */
    Integer length;
    /**
     * 整数部分
     */
    Integer precision;
    /**
     * 小数部分
     */
    Integer scale;
    /**
     * 数据库字段类型 只是数据类型
     */
    String jdbcType;
    /**
     * 是否允许为空
     */
    Boolean nullable = true;
    /**
     * 枚举类,如果无枚举字段类型为Object
     */
    Class enumClass = Object.class;
    /**
     * 字段注释
     */
    String comment = "";
    /**
     * 主键生成策略
     */
    PrimaryKeyStrategy primaryKeyStrategy = PrimaryKeyStrategy.AUTO;
    /**
     * 主键生成时候的特征
     */
    String primaryKeyFeature;
    /**
     * 默认值
     */
    String defaultValue = "";
    /**
     * 如果字段为物理主键时，是否为自增字段
     */
    Boolean autoIncrement = false;
    /**
     * 作为条件时的值模式
     */
    ValueMode valueMode = ValueMode.EQUAL;

    public ColumnMetadata(boolean primaryKey, TableMetadata tableMetadata, Class entityClass, Field columnField, String javaName, Class javaType, String jdbcName, String fullJdbcType, String jdbcType, Boolean nullable, Class enumClass, String comment, PrimaryKeyStrategy primaryKeyStrategy, String primaryKeyFeature, String defaultValue, Boolean autoIncrement, ValueMode valueMode) {
        this.primaryKey = primaryKey;
        this.tableMetadata = tableMetadata;
        this.entityClass = entityClass;
        this.columnField = columnField;
        this.javaName = javaName;
        this.javaType = javaType;
        this.jdbcName = jdbcName;
        this.fullJdbcType = fullJdbcType;
        this.jdbcType = jdbcType;
        this.nullable = nullable;
        this.enumClass = enumClass;
        this.comment = comment;
        this.primaryKeyStrategy = primaryKeyStrategy;
        this.primaryKeyFeature = primaryKeyFeature;
        this.defaultValue = defaultValue;
        this.autoIncrement = autoIncrement == null ? false : autoIncrement;
        this.valueMode = valueMode == null ? ValueMode.EQUAL : valueMode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ColumnMetadata{");
        sb.append("primaryKey=").append(primaryKey);
        sb.append(", tableMetadata=").append(tableMetadata);
        sb.append(", entityClass=").append(entityClass);
        sb.append(", columnField=").append(columnField);
        sb.append(", javaName='").append(javaName).append('\'');
        sb.append(", javaType=").append(javaType);
        sb.append(", jdbcName='").append(jdbcName).append('\'');
        sb.append(", fullJdbcType='").append(fullJdbcType).append('\'');
        sb.append(", jdbcType='").append(jdbcType).append('\'');
        sb.append(", nullable=").append(nullable);
        sb.append(", enumClass=").append(enumClass);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", primaryKeyStrategy=").append(primaryKeyStrategy);
        sb.append(", primaryKeyFeature='").append(primaryKeyFeature).append('\'');
        sb.append(", defaultValue='").append(defaultValue).append('\'');
        sb.append(", autoIncrement=").append(autoIncrement);
        sb.append(", valueMode=").append(valueMode);
        sb.append('}');
        return sb.toString();
    }
}