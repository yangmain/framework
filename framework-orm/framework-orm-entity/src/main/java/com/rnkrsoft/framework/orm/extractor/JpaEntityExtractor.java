package com.rnkrsoft.framework.orm.extractor;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.orm.jdbc.Ignore;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Created by rnkrsoft.com on 2017/1/7.
 * 提取标注JPA注解的实体类
 */
@Slf4j
public class JpaEntityExtractor implements EntityExtractor {

    public EntityExtractor extractTable(TableMetadata tableMetadata) {
        javax.persistence.Table tableAnn = (javax.persistence.Table) tableMetadata.getEntityClass().getAnnotation(javax.persistence.Table.class);
        //提取表名
        if (tableAnn != null && tableAnn.name() != null && !tableAnn.name().trim().isEmpty()) {
            tableMetadata.setTableName(tableAnn.name().trim());
            tableMetadata.setSchema(tableAnn.schema());
        }
        return this;
    }

    public EntityExtractor extractFieldString(ColumnMetadata columnMetadata) {
        Column column = columnMetadata.getColumnField().getAnnotation(Column.class);
        String dataType = null;
        String jdbcType = null;
        if (column != null) {
            if (column.length() <= 0) {
                ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息中{}字段", columnMetadata.getEntityClass(), columnMetadata.getJavaName())
                        .message("字段{}为VARCHAR类型，但是没有指定length", columnMetadata.getJdbcName())
                        .solution("在标注{}注解上的属性{}设置字符串长度", Column.class, "length")
                        .throwError();
            } else if (column.length() > 255) {
                log.warn("实体[{}]字段[{}]的文本长度超过了256，自动使用TEXT数据类型", columnMetadata.getEntityClass().getName(), columnMetadata.getJdbcName());
                dataType = "TEXT";
                jdbcType = "VARCHAR";
            } else {
                dataType = "VARCHAR(" + column.length() + ")";
                jdbcType = "VARCHAR";
            }
        }
        if (jdbcType != null) {
            columnMetadata.setJdbcType(jdbcType);
        }
        if (dataType != null) {
            columnMetadata.setDataType(dataType);
        }
        return this;
    }


    public EntityExtractor extractFieldNumber(ColumnMetadata columnMetadata) {
        Column column = columnMetadata.getColumnField().getAnnotation(Column.class);
        Class fieldClass = columnMetadata.getJavaType();
        String dataType = null;
        String jdbcType = null;
        if (column != null) {
            if (fieldClass == BigDecimal.class) {
                if (column.scale() > 0 && column.precision() > 0 && column.precision() > column.scale()) {
                    dataType = "DECIMAL(" + column.precision() + "," + column.scale() + ")";
                } else if (column.precision() > 0 && column.scale() == 0) {
                    dataType = "DECIMAL(" + column.precision() + ")";
                } else if (column.precision() > column.scale() && column.scale() == 0) {
                    dataType = "DECIMAL(18)";
                } else {
                    dataType = "DECIMAL(18,2)";
                }
                jdbcType = "DECIMAL";
            } else if (fieldClass == Long.class) {
                dataType = "BIGINT";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息中{}字段", columnMetadata.getEntityClass(), columnMetadata.getJavaName())
                            .message("字段{}为整型包装类型，不允许为空", columnMetadata.getJdbcName())
                            .solution("在标注{}注解上的属性{}设置为{}", Column.class, "nullable", false)
                            .throwError();
                }
            } else if (fieldClass == Long.TYPE) {
                dataType = "BIGINT";
                jdbcType = "NUMERIC";
            } else if (fieldClass == Integer.class) {
                dataType = "INTEGER";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息中{}字段", columnMetadata.getEntityClass(), columnMetadata.getJavaName())
                            .message("字段{}为整型包装类型，不允许为空", columnMetadata.getJdbcName())
                            .solution("在标注{}注解上的属性{}设置为{}", Column.class, "nullable", false)
                            .throwError();
                }
            } else if (fieldClass == Integer.TYPE) {
                dataType = "INTEGER";
                jdbcType = "NUMERIC";
            } else if (fieldClass == Boolean.class) {
                dataType = "TINYINT";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息中{}字段", columnMetadata.getEntityClass(), columnMetadata.getJavaName())
                            .message("字段{}为布尔值包装类型，不允许为空", columnMetadata.getJdbcName())
                            .solution("在标注{}注解上的属性{}设置为{}", Column.class, "nullable", false).throwError();
                }
            } else if (fieldClass == Boolean.TYPE) {
                dataType = "TINYINT";
                jdbcType = "NUMERIC";
            } else {
            }
        }
        if (jdbcType != null) {
            columnMetadata.setJdbcType(jdbcType);
        }
        if (dataType != null) {
            columnMetadata.setDataType(dataType);
        }
        return this;
    }


    public EntityExtractor extractFieldDate(ColumnMetadata columnMetadata) {
        Column column = columnMetadata.getColumnField().getAnnotation(Column.class);
        Class fieldClass = columnMetadata.getJavaType();
        String dataType = null;
        String jdbcType = null;
        if (column != null) {
            if (fieldClass == java.util.Date.class) {
                Temporal temporal = (Temporal) fieldClass.getAnnotation(Temporal.class);
                //如果没有注解则直接使用TIMESTAMP,数据库用DATETIME
                if (temporal == null) {
                    dataType = "DATETIME";
                    jdbcType = "TIMESTAMP";
                } else {
                    //如果有注解指定数据类型，则使用指定的数据类型
                    if (TemporalType.TIMESTAMP == temporal.value()) {
                        dataType = "TIMESTAMP";
                        jdbcType = "TIMESTAMP";
                    } else if (TemporalType.TIME == temporal.value()) {
                        dataType = "TIME";
                        jdbcType = "TIME";
                    } else if (TemporalType.DATE == temporal.value()) {
                        dataType = "DATE";
                        jdbcType = "DATE";
                    }
                }
            } else if (fieldClass == java.sql.Timestamp.class) {
                dataType = "TIMESTAMP";
                jdbcType = "TIMESTAMP";
            } else if (fieldClass == java.sql.Time.class) {
                dataType = "TIME";
                jdbcType = "TIME";
            } else if (fieldClass == java.sql.Date.class) {
                dataType = "DATE";
                jdbcType = "DATE";
            }
        }
        if (jdbcType != null) {
            columnMetadata.setJdbcType(jdbcType);
        }
        if (dataType != null) {
            columnMetadata.setDataType(dataType);
        }
        return this;
    }


    /**
     * 提取字段上的主键信息
     *
     * @param columnMetadata 元信息
     */
    public EntityExtractor extractFieldPrimaryKey(ColumnMetadata columnMetadata) {
        Field field = columnMetadata.getColumnField();
        Id id = columnMetadata.getColumnField().getAnnotation(Id.class);
        PrimaryKeyStrategy primaryKeyStrategy = PrimaryKeyStrategy.AUTO;
        if (id == null) {
            columnMetadata.setPrimaryKeyStrategy(primaryKeyStrategy);
            return this;
        }
        GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        if (generatedValue != null) {
            if (generatedValue.strategy() == GenerationType.AUTO) {
                if (columnMetadata.getJavaType() == Integer.class || columnMetadata.getJavaType() == Integer.TYPE) {
                    primaryKeyStrategy = PrimaryKeyStrategy.IDENTITY;
                }
            } else if (generatedValue.strategy() == GenerationType.IDENTITY) {
                if (columnMetadata.getJavaType() != Integer.class && columnMetadata.getJavaType() != Integer.TYPE) {
                    ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}使用了自增主键，类型必须为整数", field.getName())
                            .solution("将字段{}的类型从{}修改为{}或者{}", field.getName(), columnMetadata.getJavaType(), Integer.class, "int").throwError();
                }
                primaryKeyStrategy = PrimaryKeyStrategy.IDENTITY;
            } else if (generatedValue.strategy() == GenerationType.StringType) {
                primaryKeyStrategy = PrimaryKeyStrategy.UUID;
            } else if (generatedValue.strategy() == GenerationType.SEQUENCE) {
                if (columnMetadata.getJavaType() != Integer.class && columnMetadata.getJavaType() != Integer.TYPE) {
                    ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}使用了自增主键，类型必须为整数", field.getName())
                            .solution("将字段{}的类型从{}修改为{}或者{}", field.getName(), columnMetadata.getJavaType(), Integer.class, "int").throwError();
                }
                primaryKeyStrategy = PrimaryKeyStrategy.SEQUENCE_SERVICE;
                columnMetadata.setPrimaryKeyFeature(generatedValue.generator());
            }else{
                //TODO
                ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                        .message("字段{}使用了不支持的物理主键", field.getName())
                        .solution("将字段{}的类型从{}修改为{}或者{}", field.getName(), columnMetadata.getJavaType(), Integer.class, "int").throwError();
            }
            if (columnMetadata.getNullable() != null && columnMetadata.getNullable()) {
                ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                        .message("字段{}是主键，该字段不允许为空", field.getName())
                        .solution("在字段{}将{}属性修改为{}", field.getName(), "nullabe", false).throwError();
            }
        }
        columnMetadata.setPrimaryKey(true);
        columnMetadata.getTableMetadata().getPrimaryKeys().add(columnMetadata.getJdbcName());
        columnMetadata.setPrimaryKeyStrategy(primaryKeyStrategy);
        return this;
    }


    /**
     * 提字段信息
     *
     * @param columnMetadata 字段元信息
     * @return 是否处理
     */
    public boolean extractField(ColumnMetadata columnMetadata) {
        Field field = columnMetadata.getColumnField();
        Class entityClass = columnMetadata.getEntityClass();
        Column column = field.getAnnotation(Column.class);
        Ignore ignore = field.getAnnotation(Ignore.class);
        if (ignore != null){
            return false;
        }
        if ("schema".equals(columnMetadata.getJavaName())) {
            return false;
        }

        //统计同一类注解使用几个
        int count = 0;
        if (column != null) {
            count++;
        }
        if (count == 0) {
            throw new IllegalArgumentException("实体 "
                    + entityClass.getName()
                    + "."
                    + field.getName()
                    + " 未按照约定标注javax.persistence.Column或com.rnkrsoft.framework.orm.StringColumn或com.rnkrsoft.framework.orm.NumberColumn或com.rnkrsoft.framework.orm.DateColumn注解");
        }

        if (count != 1) {
            throw new IllegalArgumentException("实体 "
                    + entityClass.getName()
                    + "."
                    + field.getName()
                    + " 未按照约定标注javax.persistence.Column或com.rnkrsoft.framework.orm.StringColumn或com.rnkrsoft.framework.orm.NumberColumn或com.rnkrsoft.framework.orm.DateColumn注解");
        }
        if (column != null) {
            if (columnMetadata.getJdbcName() == null) {
                columnMetadata.setJdbcName(column.name());
            }
            if (columnMetadata.getNullable() == null) {
                columnMetadata.setNullable(column.nullable());
            }
        }
        extractFieldString(columnMetadata);
        extractFieldNumber(columnMetadata);
        extractFieldDate(columnMetadata);
        extractFieldPrimaryKey(columnMetadata);
        return true;
    }

}
