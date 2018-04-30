package com.rnkrsoft.framework.test.junit.listener.createtable;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import com.rnkrsoft.framework.test.datasource.DataSourceScanner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/4/30.
 */
public class CreateTableHandler {


    public void create(Connection connection, CreateTableContext context) throws SQLException {
        Collection<CreateTableWrapper> entities = context.getTables().values();
        String dsn = DataSourceScanner.lookup();

        for (CreateTableWrapper createTableWrapper : entities) {
            //如果内存数据库则设置没有模式
            if (DataSourceScanner.H2_DATASOURCE.equals(dsn)){
                if (createTableWrapper.getSchemaMode() == NameMode.auto){
                    createTableWrapper.setSchemaMode(NameMode.customize);
                    context.setSchemaMode(NameMode.customize);
                    createTableWrapper.getMetadata().setSchema(null);
                }
                createTableWrapper.setSchema(null);
                context.setSchema(null);
                if(createTableWrapper.getPrefixMode() == NameMode.auto){
                    createTableWrapper.setPrefixMode(NameMode.customize);
                    createTableWrapper.getMetadata().setPrefix(createTableWrapper.getPrefix());
                    context.setPrefixMode(NameMode.customize);
                }
                context.setPrefix(createTableWrapper.getPrefix());
                if (createTableWrapper.getSuffixMode() == NameMode.auto) {
                    createTableWrapper.setSuffixMode(NameMode.customize);
                    createTableWrapper.getMetadata().setSuffix(createTableWrapper.getSuffix());
                    context.setSuffixMode(NameMode.customize);
                }
                context.setSuffix(createTableWrapper.getSuffix());
            }
            if (createTableWrapper.isTestBeforeDrop()) {
                String dropSql = SqlScriptUtils.generateDropTable(createTableWrapper.entityClass, createTableWrapper.getSchemaMode(), createTableWrapper.getSchema(), createTableWrapper.getPrefixMode(), createTableWrapper.getPrefix(), createTableWrapper.getSuffixMode(), createTableWrapper.getSuffix(), createTableWrapper.getSqlMode(), createTableWrapper.getKeywordMode(), createTableWrapper.isTestBeforeDrop());
                executeSql(connection, dropSql);
            }
            String createSql = SqlScriptUtils.generateCreateTable(createTableWrapper.entityClass, createTableWrapper.getSchemaMode(), createTableWrapper.getSchema(), createTableWrapper.getPrefixMode(), createTableWrapper.getPrefix(), createTableWrapper.getSuffixMode(), createTableWrapper.getSuffix(), null, createTableWrapper.getSqlMode(), createTableWrapper.getKeywordMode(), createTableWrapper.isCreateBeforeTest());
            executeSql(connection, createSql);
        }
    }

    public void drop(Connection connection, CreateTableContext context) throws SQLException {
        Collection<CreateTableWrapper> entities = context.getTables().values();
        String dsn = DataSourceScanner.lookup();
        if (DataSourceScanner.MYSQL_DATASOURCE.equals(dsn)){
            return;
        }
        for (CreateTableWrapper createTableWrapper : entities) {
            String dropSql = SqlScriptUtils.generateDropTable(createTableWrapper.entityClass, createTableWrapper.getSchemaMode(), createTableWrapper.getSchema(), createTableWrapper.getPrefixMode(), createTableWrapper.getPrefix(), createTableWrapper.getSuffixMode(), createTableWrapper.getSuffix(), createTableWrapper.getSqlMode(), createTableWrapper.getKeywordMode(), createTableWrapper.isTestBeforeDrop());
            executeSql(connection, dropSql);
        }
    }

    void executeSql(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            System.out.println(sql);
            statement.execute(sql);
        } finally {
            statement.close();
        }
    }
}
