package com.rnkrsoft.framework.orm.mybatis.plugins;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Invoker;
import com.rnkrsoft.reflection4j.Reflector;
import com.rnkrsoft.framework.orm.OrderBy;
import com.rnkrsoft.framework.orm.OrderByColumn;
import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class OrderByInterceptor implements Interceptor {
    WordMode keywordMode = WordMode.lowerCase;
    WordMode sqlMode = WordMode.lowerCase;

    public OrderByInterceptor() {
    }

    public OrderByInterceptor(WordMode keywordMode, WordMode sqlMode) {
        this.keywordMode = keywordMode;
        this.sqlMode = sqlMode;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql;
        int deep = 0;
        while (true || ++deep <= 50) {
            if (handler instanceof RoutingStatementHandler) {
                //通过反射获取到当前RoutingStatementHandler对象的delegate属性
                //获取到当前StatementHandler的 boundSql，这里不管是调用handler.getBoundSql()还是直接调用delegate.getBoundSql()结果是一样的，因为之前已经说过了
                //RoutingStatementHandler实现的所有StatementHandler接口方法里面都是调用的delegate对应的方法。
                handler = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
                break;
            } else {
                Plugin plugin = (Plugin) ReflectUtil.getFieldValue(handler, "h");
                handler = (StatementHandler) ReflectUtil.getFieldValue(plugin, "target");
            }
        }
        if (deep > 50) {
            throw ErrorContextFactory.instance().message("插件深度超过50").runtimeException();
        }
        boundSql = handler.getBoundSql();
        //拿到当前绑定Sql的参数对象，就是我们在调用对应的Mapper映射语句时所传入的参数对象
        Object obj = boundSql.getParameterObject();
        if (obj != null) {
            if (obj instanceof Pagination) {
                Pagination page = (Pagination) obj;
                obj = page.getEntity();
            }
        }
        if (obj != null) {
            //通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(handler, "mappedStatement");
            if (mappedStatement != null) {
                String id = mappedStatement.getId();
                int idx = id.lastIndexOf(".");
                if (obj instanceof OrderBy && idx + 1 < id.length() && mappedStatement.getId().startsWith("select", idx + 1)) {
                    Reflector reflector = GlobalSystemMetadata.reflector(obj.getClass());
                    Invoker invoker = reflector.getGetter("orderByColumns");
                    List<OrderByColumn> orderByColumns = invoker.invoke(obj);
                    String sql = boundSql.getSql();
                    //修改SQL，在后面追加 order by语句
                    StringBuilder builder = new StringBuilder(sql);
                    //如果有排序字段
                    if (!orderByColumns.isEmpty()) {
                        //排序
                        builder.append(KeywordsUtils.convert(" order by ", keywordMode)).append(convertOrderBy(orderByColumns));
                        //利用反射设置当前BoundSql对应的sql属性为拼接上排序字段的
                        ReflectUtil.setFieldValue(boundSql, "sql", builder.toString());
                    }
//                if (existPage){
//                    //通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
//                    MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(handler, "mappedStatement");
//                    SqlSource sqlSource = mappedStatement.getSqlSource();
//                    if (sqlSource instanceof RawSqlSource){
//                        //TODO
//                    }else if(sqlSource instanceof DynamicSqlSource){
//                        DynamicSqlSource dynamicSqlSource = (DynamicSqlSource)sqlSource;
//
//                        //TODO
//                    }else if(sqlSource instanceof StaticSqlSource){
//                        //TODO
//                    }
//                }
                }
            }
        }
        return invocation.proceed();
    }

    String convertOrderBy(List<OrderByColumn> orderByColumns) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < orderByColumns.size(); i++) {
            OrderByColumn value = orderByColumns.get(i);
            builder.append(KeywordsUtils.convert(value.getColumn(), sqlMode)).append(" ").append(KeywordsUtils.convert(value.getOrder().getCode(), keywordMode));
            if (i + 1 < orderByColumns.size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
