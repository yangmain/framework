package com.rnkrsoft.framework.toolkit.jdbc;

import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.SupportedJdbcType;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/4/26.
 */
@Slf4j
public class JdbcReverseMySQL implements JdbcReverse {

    public static Connection connectMySQL(String url, String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未配置MySQL驱动");
        }
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<TableMetadata> reverses(String url, String schema, String userName, String password, String packageName, String[] prefixes, String[] suffixes) throws Exception {
        Connection connection = connectMySQL("jdbc:mysql://" + url + "/" + schema, userName, password);
        Statement statement = connection.createStatement();
        List<TableMetadata> metadatas = new ArrayList();
        String sql = "select table_name as table_name, engine as table_engine, auto_increment as auto_increment, table_comment as table_comment  " +
                "from information_schema.tables " +
                "where table_schema = '" + schema + "'";
        boolean succ = statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        Set<String> prefixes0 = new HashSet();
        Set<String> suffixes0 = new HashSet();
        if (prefixes != null) {
            for (String value : prefixes) {
                prefixes0.add(value.toUpperCase());
            }
        }
        if (suffixes != null) {
            for (String value : suffixes) {
                suffixes0.add(value.toUpperCase());
            }
        }
        System.out.println(prefixes0);
        System.out.println(suffixes0);
        while (resultSet.next()) {
            String name = resultSet.getString("table_name").toUpperCase();
            int dotIndex = name.indexOf("_");
            int lastDotIndex = name.lastIndexOf("_");
            String prefix = name.substring(0, dotIndex).toUpperCase();
            String suffix = name.substring(lastDotIndex + 1).toUpperCase();
            String name0 = name;
            if (prefixes0.contains(prefix)) {
                if (dotIndex + 1 >= name.length()) {
                    log.warn("table '{}' has not prefix", name);
                    suffix = "";
                } else {
                    name0 = name.substring(dotIndex + 1);
                }
            } else {
                log.warn("table '{}' has not prefix", name);
                prefix = "";
                dotIndex = 0;
            }
            if (suffixes0.contains(suffix)) {
                if (dotIndex + 1 >= lastDotIndex) {
                    log.warn("table '{}' has not suffix", name);
                    suffix = "";
                } else {
                    //截取不含前后缀的表名
                    name0 = name.substring(dotIndex + 1, lastDotIndex);
                }
            } else {
                log.warn("table '{}' has not suffix", name);
                suffix = "";
            }
            String engine = resultSet.getString("table_engine");
            Long autoIncrement = resultSet.getLong("auto_increment");
            String comment = resultSet.getString("table_comment");
            TableMetadata tableMetadata = TableMetadata.builder()
                    .tableName(name0)
                    .entityClassName(packageName + ".entity." + StringUtils.firstCharToUpper(StringUtils.underlineToCamel(name0)) + "Entity")
                    .daoClassName(packageName + ".dao." + StringUtils.firstCharToUpper(StringUtils.underlineToCamel(name0)) + "DAO")
                    .mapperName(packageName + ".mapper." + StringUtils.firstCharToUpper(StringUtils.underlineToCamel(name0)) + "Mapper")
                    .autoIncrement(autoIncrement)
                    .prefix(prefix.toUpperCase())
                    .suffix(suffix.toUpperCase())
                    .dataEngine(engine)
                    .comment(comment)
                    .build();
            reverse(tableMetadata, schema, connection);
            metadatas.add(tableMetadata);
            System.out.println(MessageFormatter.format("reverse table - {}", name));
            log.info("reverse table - {}", name);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return metadatas;
    }

    void reverse(TableMetadata tableMetadata, String schema, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select cols.column_name as column_name, " +
                "cols.column_default as column_default, " +
                "cols.is_nullable as nullable, " +
                "cols.data_type as data_type, " +
                "cols.column_type as full_jdbc_type, " +
                "cols.extra as extra, " +
                "cols.character_maximum_length as character_maximum_length, " +
                "cols.numeric_precision as numeric_precision, " +
                "cols.numeric_scale as numeric_scale, " +
                "cols.column_key as column_key, " +
                "cols.column_comment as column_comment " +
                "from information_schema.columns cols " +
                "where upper(cols.table_schema) = upper('" + schema + "') and upper(table_name) = upper('" + tableMetadata.getFullTableName() + "') order by cols.ordinal_position";
        //执行数据SQL获取表字段信息
        boolean succ = statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            String column_name = resultSet.getString("column_name").toUpperCase();
            String column_default = resultSet.getString("column_default");
            String nullable = resultSet.getString("nullable").toUpperCase();
            SupportedJdbcType data_type = SupportedJdbcType.valueOfCode(resultSet.getString("data_type").toUpperCase());
            String full_jdbc_type = "VARCHAR(255)";
            int character_maximum_length = (int) resultSet.getLong("character_maximum_length");
            int numeric_precision = resultSet.getInt("numeric_precision");
            int numeric_scale = resultSet.getInt("numeric_scale");
            String extra = resultSet.getString("extra");
            String column_key = resultSet.getString("column_key");
            String column_comment = resultSet.getString("column_comment");
            Class java_type = String.class;
            if (data_type == SupportedJdbcType.BIGINT) {
                java_type = Long.class;
                full_jdbc_type = data_type.getCode();
            } else if (data_type == SupportedJdbcType.INT || data_type == SupportedJdbcType.INTEGER) {
                data_type= SupportedJdbcType.INTEGER;
                java_type = Integer.class;
                full_jdbc_type = SupportedJdbcType.INTEGER.getCode();
            } else if (data_type == SupportedJdbcType.SMALLINT) {
                java_type = Integer.class;
                full_jdbc_type = SupportedJdbcType.INTEGER.getCode();
            } else if (data_type == SupportedJdbcType.TINYINT) {
                java_type = Integer.class;
                full_jdbc_type = SupportedJdbcType.INTEGER.getCode();
            } else if (data_type == SupportedJdbcType.DECIMAL) {
                java_type = BigDecimal.class;
                full_jdbc_type = SupportedJdbcType.DECIMAL.getCode() + "(" + (numeric_precision == 0 ? 18 : numeric_precision) + "," + (numeric_scale == 0 ? 2 : numeric_scale) + ")";
            } else if (data_type == SupportedJdbcType.TIMESTAMP) {
                java_type = java.sql.Timestamp.class;
                full_jdbc_type = SupportedJdbcType.TIMESTAMP.getCode();
            } else if (data_type == SupportedJdbcType.VARCHAR) {
                java_type = String.class;
                full_jdbc_type = SupportedJdbcType.VARCHAR.getCode() + "(" + (character_maximum_length == 0 ? 255 : character_maximum_length) + ")";
            } else if (data_type == SupportedJdbcType.CHAR) {
                java_type = String.class;
                full_jdbc_type = SupportedJdbcType.CHAR.getCode() + "(" + (character_maximum_length == 0 ? 255 : character_maximum_length) + ")";
            } else {
                java_type = String.class;
                full_jdbc_type = SupportedJdbcType.VARCHAR.getCode() + "(" + (character_maximum_length == 0 ? 255 : character_maximum_length) + ")";
            }

            ColumnMetadata.ColumnMetadataBuilder builder = ColumnMetadata.builder()
                    .javaName(StringUtils.underlineToCamel(column_name))
                    .tableMetadata(tableMetadata)
                    .jdbcName(column_name.toUpperCase())
                    .javaType(java_type)
                    .jdbcType(data_type)
                    .fullJdbcType(full_jdbc_type.toUpperCase())
                    .defaultValue(column_default)
                    .nullable("YES".equalsIgnoreCase(nullable))
                    .length(character_maximum_length)
                    .scale(numeric_scale)
                    .precision(numeric_precision)
                    .comment(column_comment);

            //如果是主键则添加到主键列表中
            if ("PRI".equalsIgnoreCase(column_key)) {
                tableMetadata.getPrimaryKeys().add(column_name);
                if (data_type == SupportedJdbcType.BIGINT || data_type == SupportedJdbcType.INT || data_type == SupportedJdbcType.INTEGER || data_type == SupportedJdbcType.SMALLINT || data_type == SupportedJdbcType.TINYINT) {
                    builder.primaryKeyStrategy(PrimaryKeyStrategy.IDENTITY);
                } else if (data_type == SupportedJdbcType.VARCHAR || data_type == SupportedJdbcType.CHAR) {
                    builder.primaryKeyStrategy(PrimaryKeyStrategy.UUID);
                } else {
                    throw ErrorContextFactory.instance().message("字段'{}.{}' 无效主键类型 '{}'", tableMetadata.getTableName(), column_name, data_type).runtimeException();
                }
                if ("auto_increment".equalsIgnoreCase(extra)) {
                    builder.autoIncrement(true);
                }
            }
            tableMetadata.getOrderColumns().add(column_name);
            tableMetadata.getColumnMetadataSet().put(column_name, builder.build());
        }
    }

}
