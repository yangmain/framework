package com.rnkrsoft.framework.test.junit.listener.createtable;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import com.rnkrsoft.framework.test.datasource.DataSourceLookup;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by rnkrsoft.com on 2018/4/30.
 */
public class CreateTableHandler {


    public void create(DataSource dataSource, CreateTableContext context) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Collection<CreateTableWrapper> entities = context.getTables().values();
        String dsn = DataSourceLookup.lookup();

        for (CreateTableWrapper createTableWrapper : entities) {
            //如果内存数据库则设置没有模式
            if (DataSourceLookup.H2_DATASOURCE.equals(dsn)){
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
                jdbcTemplate.execute(dropSql);
            }
            String createSql = SqlScriptUtils.generateCreateTable(createTableWrapper.entityClass, createTableWrapper.getSchemaMode(), createTableWrapper.getSchema(), createTableWrapper.getPrefixMode(), createTableWrapper.getPrefix(), createTableWrapper.getSuffixMode(), createTableWrapper.getSuffix(), null, createTableWrapper.getSqlMode(), createTableWrapper.getKeywordMode(), createTableWrapper.isCreateBeforeTest());
            jdbcTemplate.execute(createSql);
        }
    }

    public void drop(DataSource dataSource, CreateTableContext context) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Collection<CreateTableWrapper> entities = context.getTables().values();
        String dsn = DataSourceLookup.lookup();
        if (DataSourceLookup.MYSQL_DATASOURCE.equals(dsn)){
            return;
        }
        for (CreateTableWrapper createTableWrapper : entities) {
            String dropSql = SqlScriptUtils.generateDropTable(createTableWrapper.entityClass, createTableWrapper.getSchemaMode(), createTableWrapper.getSchema(), createTableWrapper.getPrefixMode(), createTableWrapper.getPrefix(), createTableWrapper.getSuffixMode(), createTableWrapper.getSuffix(), createTableWrapper.getSqlMode(), createTableWrapper.getKeywordMode(), createTableWrapper.isTestBeforeDrop());
            jdbcTemplate.execute(dropSql);
        }
    }
}
