package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoValueMode;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.MetaObject;
import org.bson.Document;

/**
 * Created by rnkrsoft.com on 2018/6/11.
 */
public class BsonUtils {
    public static Document and(Object entity, boolean nullable) {
        Document document = new Document();
        MetaObject metaObject = GlobalSystemMetadata.forObject(entity.getClass(), entity);
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(entity.getClass());
        for (String getterName : metaObject.getGetterNames()) {
            Object value = metaObject.getValue(getterName);
            ColumnMetadata columnMetadata = tableMetadata.getColumnByJavaName(getterName);
            String columnName = columnMetadata.getJdbcName();
            if (value == null) {
                if (nullable) {
                    document.append(columnName, value);
                }
            } else {
                document.append(columnName, value);
            }

        }
        return document;
    }

    public static Document valueOf(Object entity , boolean nullable){
        Document document = new Document();
        MetaObject metaObject = GlobalSystemMetadata.forObject(entity.getClass(), entity);
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(entity.getClass());
        for (String getterName : metaObject.getGetterNames()) {
            Object value = metaObject.getValue(getterName);
            ColumnMetadata columnMetadata = tableMetadata.getColumnByJavaName(getterName);
            String columnName = columnMetadata.getJdbcName();
            if (value == null) {
                if (nullable) {
                    document.append(columnName, value);
                }
            } else {
                ValueMode valueMode = columnMetadata.getValueMode();
                if (valueMode == ValueMode.EQUAL) {
                    document.append(columnName, value);
                }else if (valueMode == ValueMode.LT
                        || valueMode == ValueMode.LTE
                        || valueMode == ValueMode.GT
                        || valueMode == ValueMode.GTE
                        || valueMode == ValueMode.NE){
                    document.append(columnName, new Document(MongoValueMode.valueOfCode(valueMode).getCode(), value));
                }else if (valueMode == ValueMode.LIKE){

                }
            }

        }
        return document;
    }

}
