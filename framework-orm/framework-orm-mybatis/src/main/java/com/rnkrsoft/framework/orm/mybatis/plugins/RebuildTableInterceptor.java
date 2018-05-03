package com.rnkrsoft.framework.orm.mybatis.plugins;

import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.Table;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by woate on 2018/5/3.
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class RebuildTableInterceptor implements Interceptor {
    OrmConfig ormConfig;

    public RebuildTableInterceptor(OrmConfig ormConfig) {
        this.ormConfig = ormConfig;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) PaginationStage1Interceptor.ReflectUtil.getFieldValue(handler, "delegate");
        BoundSql boundSql = delegate.getBoundSql();
        Object obj = boundSql.getParameterObject();
        Class entityClass = obj.getClass();

        //TODO
        if (ormConfig.isRebuildTable() && entityClass.getAnnotation(Table.class) != null){
            Connection connection = (Connection) invocation.getArgs()[0];
            Statement statement = connection.createStatement();
            String dropTableSql = SqlScriptUtils.generateDropTable(entityClass,
                    ormConfig.getGlobal().getSchemaMode(),
                    ormConfig.getGlobal().getSchema(),
                    ormConfig.getGlobal().getPrefixMode(),
                    ormConfig.getGlobal().getPrefix(),
                    ormConfig.getGlobal().getSuffixMode(),
                    ormConfig.getGlobal().getSuffix(),
                    ormConfig.getKeywordMode(),
                    ormConfig.getSqlMode(),
                    true);
            statement.execute(dropTableSql);
            String createTableSql = SqlScriptUtils.generateCreateTable(entityClass,
                    ormConfig.getGlobal().getSchemaMode(),
                    ormConfig.getGlobal().getSchema(),
                    ormConfig.getGlobal().getPrefixMode(),
                    ormConfig.getGlobal().getPrefix(),
                    ormConfig.getGlobal().getSuffixMode(),
                    ormConfig.getGlobal().getSuffix(),
                    null,
                    ormConfig.getKeywordMode(),
                    ormConfig.getSqlMode(),
                    true);
            statement.execute(createTableSql);
        }
        Object result = invocation.proceed();
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
