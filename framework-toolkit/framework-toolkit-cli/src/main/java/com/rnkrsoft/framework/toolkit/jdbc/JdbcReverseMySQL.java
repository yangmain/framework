package com.rnkrsoft.framework.toolkit.jdbc;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.message.MessageFormatter;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;

import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/26.
 */
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
    public List<TableMetadata> reverses(String url, String schema, String userName, String password, String packageName, String prefix, String suffix) throws Exception {
        Connection connection = connectMySQL("jdbc:mysql://" + url + "/" + schema, userName, password);
        Statement statement = connection.createStatement();
        List<TableMetadata> metadatas = new ArrayList();
        String sql = "select table_name as table_name, engine as table_engine, auto_increment as auto_increment, table_comment as table_comment  " +
                "from information_schema.tables " +
                "where table_schema = '" + schema + "'";
        boolean succ = statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        prefix = prefix == null ? "" : prefix.toUpperCase();
        suffix = suffix == null ? "" : suffix.toUpperCase();
        String prefix0 = prefix.isEmpty() ? "" : (prefix + "_");
        String suffix0 = suffix.isEmpty() ? "" : ("_" + suffix);
        while (resultSet.next()) {
            String name = resultSet.getString("table_name").toUpperCase();
            String name0 = name.toUpperCase();
            if (name0.startsWith(prefix0)) {
                name0 = name0.substring(prefix0.length());
            }
            if (name0.endsWith(suffix0)) {
                name0 = name0.substring(0, name0.length() - suffix0.length());
            }
            String engine = resultSet.getString("table_engine");
            String autoIncrement = resultSet.getString("auto_increment");
            String comment = resultSet.getString("table_comment");
            TableMetadata tableMetadata = TableMetadata.builder()
                    .tableName(name0)
                    .entityClassName(packageName + ".entity." + StringUtils.firstCharToUpper(StringUtils.underlineToCamel(name0)) + "Entity")
                    .daoClassName(packageName + ".dao." + StringUtils.firstCharToUpper(StringUtils.underlineToCamel(name0)) + "DAO")
                    .prefix(prefix.toUpperCase())
                    .suffix(suffix.toUpperCase())
                    .dataEngine(engine)
                    .comment(comment)
                    .schema(schema)
                    .build();
            reverse(tableMetadata, schema, connection);
            metadatas.add(tableMetadata);
            System.out.println(MessageFormatter.format("reverse table - {}", name));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return metadatas;
    }

    void reverse(TableMetadata tableMetadata, String schema, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select cols.column_name as column_name, cols.column_default as column_default, cols.is_nullable as is_nullable, cols.data_type as data_type, cols.column_type as column_type, extra as extra, cols.column_key as column_key, cols.column_comment as column_comment " +
                "from information_schema.columns cols " +
                "where cols.table_schema = '" + schema + "' and table_name = '" + tableMetadata.getTableName() + "' order by cols.ordinal_position";
        //执行数据SQL获取表字段信息
        boolean succ = statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            String column_name = resultSet.getString("column_name");
            String column_default = resultSet.getString("column_default");
            String is_nullable = resultSet.getString("is_nullable");
            String data_type = resultSet.getString("data_type");
            String extra = resultSet.getString("extra");
            String column_type = resultSet.getString("column_type");
            String column_key = resultSet.getString("column_key");
            String column_comment = resultSet.getString("column_comment");
            String jdbc_type = null;
            if (data_type.equals("int")) {
                jdbc_type = "NUMERIC";
                column_type = "INTEGER";
            } else if (data_type.equals("tinyint")) {
                jdbc_type = "NUMERIC";
                column_type = "TINYINT";
            } else if (data_type.equals("decimal")) {
                jdbc_type = "NUMERIC";
                column_type = "DECIMAL";
            } else if (data_type.equals("datetime")) {
                jdbc_type = "TIMESTAMP";
                column_type = "TIMESTAMP";
            } else if (data_type.equals("tinyint")) {
                jdbc_type = "VARCHAR";
                column_type = "VARCHAR";
            } else if (data_type.equals("varchar")) {
                jdbc_type = "VARCHAR";
                column_type = "VARCHAR";
            } else if (data_type.equals("char")) {
                jdbc_type = "CHAR";
                column_type = "CHAR";
            } else {
                jdbc_type = "VARCHAR";
                column_type = "VARCHAR";
            }

            ColumnMetadata.ColumnMetadataBuilder builder = ColumnMetadata.builder()
                    .javaName(StringUtils.underlineToCamel(column_name))
                    .tableMetadata(tableMetadata)
                    .jdbcName(column_name.toUpperCase())
                    .defaultValue(column_default)
                    .nullable("YES".equals(is_nullable))
                    .jdbcType(jdbc_type.toUpperCase())
                    .dataType(column_type.toUpperCase())
                    .comment(column_comment);

            //如果是主键则添加到主键列表中
            if (column_key != null && "PRI".equalsIgnoreCase(column_key)) {
                tableMetadata.getPrimaryKeys().add(column_name);
                if (data_type.contentEquals("int") || data_type.contentEquals("tinyint")) {
                    builder.primaryKeyStrategy(PrimaryKeyStrategy.IDENTITY);
                } else if (data_type.contentEquals("varchar") || data_type.contentEquals("char")) {
                    builder.primaryKeyStrategy(PrimaryKeyStrategy.UUID);
                } else {
                    throw ErrorContextFactory.instance().message("无效主键类型").runtimeException();
                }
            }
            tableMetadata.getOrderColumns().add(column_name);
            tableMetadata.getColumnMetadataSet().put(column_name, builder.build());
        }
    }

}
