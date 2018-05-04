package com.rnkrsoft.framework.toolkit.jdbc;

import com.rnkrsoft.framework.orm.metadata.TableMetadata;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/26.
 * JDBC逆向器
 */
public interface JdbcReverse {
    /**
     * 逆向生成表信息
     *
     * @param url      主机
     * @param schema   数据库模式
     * @param userName 用户名
     * @param password 密码
     * @param prefix   前缀
     * @param suffix   后缀
     * @return 表信息列表
     */
    List<TableMetadata> reverses(String url, String schema, String userName, String password, String packageName, String prefix, String suffix) throws Exception;
}
