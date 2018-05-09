package com.rnkrsoft.framework.orm.mybatis.plugins;

import com.devops4j.reflection4j.GlobalSystemMetadata;
import com.devops4j.reflection4j.Invoker;
import com.devops4j.reflection4j.MetaObject;
import com.devops4j.reflection4j.Reflector;
import com.rnkrsoft.framework.orm.OrderBy;
import com.rnkrsoft.framework.orm.OrderByColumn;
import com.rnkrsoft.framework.orm.Pagination;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class OrderByInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql;
        if (handler instanceof RoutingStatementHandler) {
            //通过反射获取到当前RoutingStatementHandler对象的delegate属性
            StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
            //获取到当前StatementHandler的 boundSql，这里不管是调用handler.getBoundSql()还是直接调用delegate.getBoundSql()结果是一样的，因为之前已经说过了
            //RoutingStatementHandler实现的所有StatementHandler接口方法里面都是调用的delegate对应的方法。
            boundSql = delegate.getBoundSql();
        } else {
            boundSql = handler.getBoundSql();
        }
        //拿到当前绑定Sql的参数对象，就是我们在调用对应的Mapper映射语句时所传入的参数对象
        Object obj = boundSql.getParameterObject();
        if (obj != null) {
            if (obj instanceof Pagination) {
                Pagination page = (Pagination) obj;
                obj = page.getEntity();
            }
        }
        if (obj != null) {
            if (obj instanceof OrderBy) {
                Reflector reflector = GlobalSystemMetadata.reflector(obj.getClass());
                Invoker invoker = reflector.getGetter("orderByColumns");
                List<OrderByColumn> orderByColumns = invoker.invoke(obj);
                String sql = boundSql.getSql();
                //修改SQL，在后面追加 order by语句
                StringBuilder builder = new StringBuilder(sql);
                //排序
                builder.append(" order by ").append(convertOrderBy(orderByColumns));
                //利用反射设置当前BoundSql对应的sql属性为拼接上排序字段的
                ReflectUtil.setFieldValue(boundSql, "sql", builder.toString());
            }

        }
        return invocation.proceed();
    }

    String convertOrderBy(List<OrderByColumn> orderByColumns) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < orderByColumns.size(); i++) {
            OrderByColumn value = orderByColumns.get(i);
            builder.append(value.getColumn()).append(" ").append(value.getOrder().getCode());
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
