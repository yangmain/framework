package com.rnkrsoft.framework.orm.untils;

import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.NameMode;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 实体工具类，用于从实体类上提取注解，生成封装
 */
@Slf4j
public abstract class SqlScriptUtils {
    /**
     * @param entityClass
     * @param schema           指定的数据库模式，如果为null，则使用元信息数据库模式
     * @param prefix
     * @param suffix
     * @param engine           指定的数据引擎，如果为null，则使用元信息数据引擎
     * @param sqlMode
     * @param keywordMode
     * @param createBeforeTest
     * @return
     */
    public static String generateCreateTable(Class entityClass,
                                             NameMode schemaMode,
                                             String schema,
                                             NameMode prefixMode,
                                             String prefix,
                                             NameMode suffixMode,
                                             String suffix,
                                             String engine,
                                             WordMode sqlMode,
                                             WordMode keywordMode,
                                             boolean createBeforeTest) {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(entityClass, false);
        if (schemaMode == NameMode.AUTO) {
            if (schema != null) {
                tableMetadata.setSchema(schema);
            }
        } else if (schemaMode == NameMode.ENTITY) {

        } else if (schemaMode == NameMode.CREATE_TEST) {
            tableMetadata.setSchema(schema);
        }
        if (prefixMode == NameMode.AUTO) {
            if (prefix != null) {
                tableMetadata.setPrefix(prefix);
            }
        } else if (prefixMode == NameMode.ENTITY) {

        } else if (prefixMode == NameMode.CREATE_TEST) {
            tableMetadata.setPrefix(prefix);
        }
        if (suffixMode == NameMode.AUTO) {
            if (suffix != null) {
                tableMetadata.setSuffix(suffix);
            }
        } else if (suffixMode == NameMode.ENTITY) {

        } else if (suffixMode == NameMode.CREATE_TEST) {
            tableMetadata.setSuffix(suffix);
        }
        tableMetadata.setDataEngine(engine);
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        try {
            generateCreateTable(os, tableMetadata, sqlMode, keywordMode, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toString();
    }

    /**
     * 生成建表语句
     *
     * @param os               输出流
     * @param tableMetadata    表元信息
     * @param sqlMode          SQL语句是否大小写
     * @param keywordMode      关键字是否大小写
     * @param createBeforeTest 建表之前是否进行测试
     */
    public static void generateCreateTable(ByteArrayOutputStream os,
                                           TableMetadata tableMetadata,
                                           WordMode sqlMode,
                                           WordMode keywordMode,
                                           boolean createBeforeTest) throws IOException {
        StringBuilder sql = new StringBuilder(convert("CREATE TABLE", keywordMode));
        if (createBeforeTest) {
            sql.append(convert(" IF NOT EXISTS", keywordMode));
        }
        String table = tableMetadata.getTableName();
        if (!StringUtils.isBlank(tableMetadata.getSchema())) {
            if (!StringUtils.isBlank(tableMetadata.getPrefix())) {
                table = tableMetadata.getPrefix() + "_" + table;
            }
            if (!StringUtils.isBlank(tableMetadata.getSuffix())) {
                table = table + "_" + tableMetadata.getSuffix();
            }
            table = tableMetadata.getSchema() + "." + table;
        }
        table = convert(table, sqlMode);
        sql.append(" " + table);
        sql.append("(\n");
        int autoIncrementCnt = 0;
        for (String name : tableMetadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = tableMetadata.getColumnMetadataSet().get(name);
            sql.append(" ")
                    .append(convert(name, sqlMode))
                    .append(" ")
                    .append(convert(columnMetadata.getDataType(), keywordMode));
            sql.append(" ");
            if (columnMetadata.getNullable()) {
                sql.append(convert("NULL", keywordMode));
            } else {
                sql.append(convert("NOT NULL", keywordMode));
            }
            sql.append(" ");
            String defval = columnMetadata.getDefaultValue();
            if (columnMetadata.getJdbcType().equals("TIMESTAMP")) {
                if (defval == null || defval.isEmpty()) {
                    defval = "'1971-01-01 00:00:00'";
                }
            } else if (columnMetadata.getJdbcType().startsWith("DECIMAL")) {
                if (defval == null || defval.isEmpty()) {
                    defval = "0";
                }
            } else if (columnMetadata.getJdbcType().startsWith("NUMERIC")) {
                if (columnMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.IDENTITY) {
                    if (defval == null || defval.isEmpty()) {
                        defval = "0";
                    }
                }
                //如果MySQL中Text是不支持默认值为空的
            } else if (columnMetadata.getJdbcType().equals("VARCHAR") && columnMetadata.getJdbcType().equals("CHAR") && !columnMetadata.getDataType().equals("TEXT")) {
                if (defval != null && !defval.isEmpty()) {
                    defval = "'" + defval + "'";
                }
            } else {

            }
            if (defval != null && !defval.isEmpty()) {
                sql.append(convert(" DEFAULT ", keywordMode) + defval + " ");
            }
            if (columnMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.IDENTITY) {
                sql.append(convert(" AUTO_INCREMENT ", keywordMode));
                autoIncrementCnt++;
            }
            if (columnMetadata.getComment() != null && !columnMetadata.getComment().trim().isEmpty()) {
                sql.append(convert(" COMMENT '", keywordMode)).append(columnMetadata.getComment()).append("'");
            }
            sql.append(",\n");
        }

        sql.append(primaryKey(tableMetadata.getPrimaryKeys(), sqlMode, keywordMode) + "\n");
        sql.append(") ");
        if (autoIncrementCnt > 1) {
            throw new IllegalArgumentException(tableMetadata.getTableName() + "自增主键只允许一个");
        }

        if (!StringUtils.isBlank(tableMetadata.getDataEngine())) {
            sql.append(convert("ENGINE=", keywordMode) + tableMetadata.getDataEngine() + " ");
        }
        //如果实体类上有注解
        if (!StringUtils.isBlank(tableMetadata.getComment())) {
            sql.append(convert("COMMENT = '", keywordMode) + tableMetadata.getComment() + "'");
        }
        os.write(sql.toString().getBytes());
    }

    public static String generateDropTable(Class entityClass,
                                           NameMode schemaMode,
                                           String schema,
                                           NameMode prefixMode,
                                           String prefix,
                                           NameMode suffixMode,
                                           String suffix,
                                           WordMode sqlMode,
                                           WordMode keywordMode,
                                           boolean dropBeforeTest) {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(entityClass, false);
        if (schemaMode == NameMode.AUTO) {
            if (schema != null) {
                tableMetadata.setSchema(schema);
            }
        } else if (schemaMode == NameMode.ENTITY) {

        } else if (schemaMode == NameMode.CREATE_TEST) {
            tableMetadata.setSchema(schema);
        }
        if (prefixMode == NameMode.AUTO) {
            if (prefix != null) {
                tableMetadata.setPrefix(prefix);
            }
        } else if (prefixMode == NameMode.ENTITY) {

        } else if (prefixMode == NameMode.CREATE_TEST) {
            tableMetadata.setPrefix(prefix);
        }
        if (suffixMode == NameMode.AUTO) {
            if (suffix != null) {
                tableMetadata.setSuffix(suffix);
            }
        } else if (suffixMode == NameMode.ENTITY) {

        } else if (suffixMode == NameMode.CREATE_TEST) {
            tableMetadata.setSuffix(suffix);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        try {
            generateDropTable(os, tableMetadata, sqlMode, keywordMode, dropBeforeTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toString();
    }

    /**
     * 生成删表语句
     *
     * @param os             输出流
     * @param tableMetadata  表元信息
     * @param sqlMode        SQL语句是否大小写
     * @param keywordMode    关键字是否大小写
     * @param dropBeforeTest 删表之前进行测试
     */
    public static void generateDropTable(ByteArrayOutputStream os,
                                         TableMetadata tableMetadata,
                                         WordMode sqlMode,
                                         WordMode keywordMode,
                                         boolean dropBeforeTest) throws IOException {
        StringBuilder sql = new StringBuilder(convert("DROP TABLE", keywordMode));
        if (dropBeforeTest) {
            sql.append(convert(" IF EXISTS", keywordMode));
        }
        sql.append(" ");
        String table = tableMetadata.getTableName();
        if (!StringUtils.isBlank(tableMetadata.getSchema())) {
            if (!StringUtils.isBlank(tableMetadata.getPrefix())) {
                table = tableMetadata.getPrefix() + "_" + table;
            }
            if (!StringUtils.isBlank(tableMetadata.getSuffix())) {
                table = table + "_" + tableMetadata.getSuffix();
            }
            table = tableMetadata.getSchema() + "." + table;
        }
        table = convert(table, sqlMode);
        sql.append(table);
        os.write(sql.toString().getBytes("UTF-8"));
    }

    /**
     * 生成截断表语句
     *
     * @param os            输出流
     * @param tableMetadata 表元信息
     * @param sqlMode       SQL语句是否大小写
     * @param keywordMode   关键字是否大小写
     */
    public static void generateTruncateTable(ByteArrayOutputStream os,
                                             TableMetadata tableMetadata,
                                             WordMode sqlMode,
                                             WordMode keywordMode) throws IOException {
        StringBuilder sql = new StringBuilder(convert("TRUNCATE TABLE", keywordMode));
        sql.append(" ");
        String table = tableMetadata.getTableName();
        if (!StringUtils.isBlank(tableMetadata.getSchema())) {
            if (!StringUtils.isBlank(tableMetadata.getPrefix())) {
                table = tableMetadata.getPrefix() + "_" + table;
            }
            if (!StringUtils.isBlank(tableMetadata.getSuffix())) {
                table = table + "_" + tableMetadata.getSuffix();
            }
            table = tableMetadata.getSchema() + "." + table;
        }
        table = convert(table, sqlMode);
        sql.append(table);
        os.write(sql.toString().getBytes("UTF-8"));
    }

    /**
     * 生成主键
     *
     * @param primaryKeys 逐渐列表
     * @param sqlMode     sql大小写模式
     * @param keywordMode 关键词大小写模式
     * @return 主键语句
     */
    static String primaryKey(List<String> primaryKeys, WordMode sqlMode, WordMode keywordMode) {
        String primaryKey = convert(" PRIMARY KEY(", keywordMode);
        for (int i = 0; i < primaryKeys.size(); i++) {
            if (i == 0) {
                primaryKey = primaryKey + convert(primaryKeys.get(i), sqlMode);
            } else {
                primaryKey = primaryKey + "," + convert(primaryKeys.get(i), sqlMode);
            }
        }
        primaryKey = primaryKey + ")";
        return primaryKey;
    }

    /**
     * 通过实体生成SQL select 后的列名
     *
     * @param entityClass 实体
     * @param keywordMode 关键大小写
     * @param sqlMode     SQL关键大小写
     * @param newline     是否换行
     * @return 列名
     */
    public static String genreateSqlHead(Class<?> entityClass, WordMode keywordMode, WordMode sqlMode, boolean newline) {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(entityClass, false);
        StringBuilder builder = new StringBuilder();
        int index = 0;
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadataSet();
        for (String column : tableMetadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = fields.get(column);
            index++;
            //如果不是第一个字段，且要换行
            if (builder.length() > 0 && newline) {
                builder.append("\n");
            }
            builder.append(convert(columnMetadata.getJdbcName(), sqlMode))
                    .append(convert(" AS ", keywordMode))
                    .append(columnMetadata.getJavaName());
            if (index != tableMetadata.getColumnMetadataSet().size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    /**
     * 生成默认模式的建表语句
     *
     * @param os
     * @param keywordMode 关键大小写
     * @param sqlMode     SQL关键大小写
     * @param entities
     * @throws IOException
     */
    public static void generateSql(ByteArrayOutputStream os,
                                          WordMode sqlMode,
                                          WordMode keywordMode,
                                          Class... entities) throws IOException {
        for (Class entityClass : entities) {
            EntityExtractorHelper helper = new EntityExtractorHelper();
            TableMetadata tableMetadata = helper.extractTable(entityClass, false);
            generateDropTable(os, tableMetadata, sqlMode, keywordMode, false);
            os.write("\n".getBytes("UTF-8"));
            generateCreateTable(os, tableMetadata, sqlMode, keywordMode, false);
            os.write("\n".getBytes("UTF-8"));
        }
    }

    /**
     * 创建SQL语句
     * @param keywordMode 关键大小写
     * @param sqlMode     SQL关键大小写
     * @param entities
     * @throws IOException
     */
    public static void generateSql(WordMode sqlMode,
                                   WordMode keywordMode,
                                   Class... entities) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        for (Class entityClass : entities) {
            EntityExtractorHelper helper = new EntityExtractorHelper();
            TableMetadata tableMetadata = helper.extractTable(entityClass, false);
            generateDropTable(os, tableMetadata, sqlMode, keywordMode, false);
            os.write("\n".getBytes("UTF-8"));
            generateCreateTable(os, tableMetadata, sqlMode, keywordMode, false);
            os.write("\n".getBytes("UTF-8"));
        }
        FileOutputStream fos = new FileOutputStream("./target/sqlScript.sql");
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    public static String convert(String sql, WordMode mode){
        if(mode == WordMode.lowerCase){
            return sql.toLowerCase();
        }else if(mode == WordMode.upperCase){
            return sql.toUpperCase();
        }else{
            return sql;
        }
    }
}
