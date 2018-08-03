package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Reflector;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 */
public class MongoEntityUtils {
    public static TableMetadata extractTable(Class entityClass) {
        MongoTable mongoCollection = (MongoTable) entityClass.getAnnotation(MongoTable.class);

        TableMetadata.TableMetadataBuilder builder = TableMetadata.builder()
                .entityClass(entityClass)
                .entityClassName(entityClass.getSimpleName());
        if (mongoCollection != null) {
            String schema = mongoCollection.schema();
            String tableName = mongoCollection.name();
            builder.tableName(tableName)
                    .schema(schema);
        }
        TableMetadata tableMetadata = builder.build();
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

    public static boolean extractField(ColumnMetadata columnMetadata) {
        Field field = columnMetadata.getColumnField();
        MongoColumn mongoColumn = field.getAnnotation(MongoColumn.class);
        PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
        String columnName = mongoColumn.name();
        columnMetadata.setJavaName(columnMetadata.getColumnField().getName());
        columnMetadata.setJavaType(field.getType());
        columnMetadata.setDefaultValue(mongoColumn.defaultValue());
        columnMetadata.setNullable(mongoColumn.nullable());
        columnMetadata.setValueMode(mongoColumn.valueMode());
        columnMetadata.setLogicMode(mongoColumn.logicMode());
        columnMetadata.setOnUpdateCurrentTimestamp(false);
        columnMetadata.setAutoIncrement(false);
        if (primaryKey != null) {
            if (columnName.isEmpty()) {
                columnName = "_id";
            }
            if (!columnName.equals("_id")) {
                throw ErrorContextFactory.instance().message("物理主键不为'_id', 实际 '{}'", columnName).runtimeException();
            }
            if (mongoColumn.nullable()) {
                if (field.getType() != ObjectId.class) {
                    throw ErrorContextFactory.instance().message("物理主键允许为空，数据类型必须为 '{}'", ObjectId.class).runtimeException();
                }
            }
            columnMetadata.setPrimaryKey(true);
            columnMetadata.setPrimaryKeyFeature(primaryKey.feature());
            columnMetadata.setPrimaryKeyStrategy(primaryKey.strategy());
            columnMetadata.getTableMetadata().getPrimaryKeys().add(columnName);
        }
        columnMetadata.setJdbcName(columnName.isEmpty() ? field.getName() : columnName);
        return true;
    }
}
