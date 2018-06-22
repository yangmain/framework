package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Reflector;

import java.lang.reflect.Field;

/**
 * Created by woate on 2018/6/21.
 */
public class MongoEntityUtils {
    public static TableMetadata extractTable(Class entityClass){
        MongoTable mongoCollection = (MongoTable) entityClass.getAnnotation(MongoTable.class);
        String schema = mongoCollection.schema();
        String tableName = mongoCollection.name();
        TableMetadata tableMetadata = TableMetadata.builder()
                .entityClass(entityClass)
                .entityClassName(entityClass.getSimpleName())
                .tableName(tableName)
                .schema(schema)
                .build();
        extractFields(tableMetadata);
        return tableMetadata;
    }

    public static void extractFields(TableMetadata tableMetadata) {
        Reflector reflector = GlobalSystemMetadata.reflector(tableMetadata.getEntityClass());
        for (Field field : reflector.getFields()) {
            //任意使用了一个字段注解的
            ColumnMetadata columnMetadata = ColumnMetadata.builder()
                    .tableMetadata(tableMetadata)
                    .entityClass(tableMetadata.getEntityClass())
                    .columnField(field)
                    .javaName(field.getName())
                    .javaType(field.getType())
                    .build();
            //提取该字段元信息
            if (!extractField(columnMetadata)) {
                continue;
            }
            //保存有序的字段
            tableMetadata.getOrderColumns().add(columnMetadata.getJdbcName());
            tableMetadata.getColumnMetadataSet().put(columnMetadata.getJdbcName(), columnMetadata);
        }
    }

    public static boolean extractField(ColumnMetadata columnMetadata){
        Field field = columnMetadata.getColumnField();
        MongoColumn mongoColumn = field.getAnnotation(MongoColumn.class);
        PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
        String columnName = mongoColumn.name();
        columnMetadata.setJavaName(columnMetadata.getColumnField().getName());
        columnMetadata.setJavaType(field.getType());
        columnMetadata.setDefaultValue(mongoColumn.defaultValue());
        if (primaryKey != null){
            if (columnName.isEmpty()){
                columnName = "_id";
            }
            if (!columnName.equals("_id")){
                throw ErrorContextFactory.instance().message("物理主键不为'_id', 实际 '{}'", columnName).runtimeException();
            }
            columnMetadata.setPrimaryKey(true);
            columnMetadata.setPrimaryKeyFeature(primaryKey.feature());
            columnMetadata.setPrimaryKeyStrategy(primaryKey.strategy());
            columnMetadata.getTableMetadata().getPrimaryKeys().add(columnName);
        }
        columnMetadata.setJdbcName(columnName);
        return true;
    }
}
