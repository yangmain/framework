package com.rnkrsoft.framework.orm.metadata;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.utils.StringUtils;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表元信息
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableMetadata {
    /**
     * 实体类对象
     */
    Class entityClass;
    /**
     * 数据访问对象
     */
    Class daoClass;
    /**
     * 实体类对象全限定名
     */
    String entityClassName = "";
    /**
     * 数据访问对象全限定名
     */
    String daoClassName = "";
    /**
     * Mapper文件名
     */
    String mapperName = "";
    /**
     * 表名
     */
    String tableName = "";
    /**
     * 备注
     */
    String comment = "";
    /**
     * 主键
     */
    final List<String> primaryKeys = new ArrayList();
    /**
     * 字段缓存
     */
    final Map<String, ColumnMetadata> columnMetadataSet = new HashMap();
    /**
     * 有序的字段名，和实体上定义的顺序一致
     */
    final List<String> orderColumns = new ArrayList();
    /**
     * 使用的数据引擎
     */
    String dataEngine;
    /**
     * 当前已自增到的数值
     */
    Long autoIncrement;
    /**
     * 数据库模式
     */
    String schema = "";
    /**
     * 表前缀
     */
    String prefix = "";
    /**
     * 表后缀
     */
    String suffix = "";

    /**
     * 增加字段定义
     * @param columnMetadata 字段元信息
     * @return 表元信息
     */
    public TableMetadata addColumn(ColumnMetadata columnMetadata){
        columnMetadataSet.put(columnMetadata.getJdbcName(), columnMetadata);
        orderColumns.add(columnMetadata.getJdbcName());
        if (columnMetadata.isPrimaryKey()){
            primaryKeys.add(columnMetadata.getJdbcName());
        }
        return this;
    }

    /**
     * 获取字段元信息
     * @param columnName 字段名
     * @return 字段元信息
     */
    public ColumnMetadata getColumn(String columnName){
        ColumnMetadata columnMetadata = columnMetadataSet.get(columnName);
        if (columnMetadata == null){
            throw ErrorContextFactory.instance().message("不存在JDBC字段'{}'", columnName).runtimeException();
        }
        return  columnMetadata;
    }

    /**
     * 获取字段元信息
     * @param javaName java字段名
     * @return 字段元信息
     */
    public ColumnMetadata getColumnByJavaName(String javaName){
        for (ColumnMetadata columnMetadata : columnMetadataSet.values()){
            if (columnMetadata.getJavaName().equals(javaName)){
                return columnMetadata;
            }
        }
        throw ErrorContextFactory.instance().message("不存在Java字段'{}'", javaName).runtimeException();
    }

    public String getFullTableName(){
        String table = tableName;
        if (!StringUtils.isBlank(prefix)) {
            table = prefix + "_" + table;
        }
        if (!StringUtils.isBlank(suffix)) {
            table = table + "_" + suffix;
        }
        if (!StringUtils.isBlank(schema)) {
            table = schema + "." + table;
        }
        return table;
    }
}
