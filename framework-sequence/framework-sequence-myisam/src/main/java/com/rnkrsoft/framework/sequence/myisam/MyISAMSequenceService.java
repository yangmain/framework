package com.rnkrsoft.framework.sequence.myisam;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.sequence.DataSourceAware;
import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SpringContextHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 */
@Slf4j
public class MyISAMSequenceService implements SequenceService, DataSourceAware{
    @Setter
    DataSource dataSource;
    JdbcTemplate jdbcTemplate;
    @Override
    public int nextval(String schema, String prefix, final String sequenceName, final String feature) {
        if (jdbcTemplate == null){
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
        String tableName = "sequence_inf";
        String sql = "insert into ";
        if (schema != null && !schema.isEmpty()) {
            sql = sql + schema + ".";
        }
        if (StringUtils.isNotBlank(prefix) && !prefix.isEmpty()){
            tableName = prefix + "_" + tableName;
        }
        sql = sql + tableName + "(seq_name, seq_feature) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int insertCnt = 0;
        final String exeSql = sql;
        try {
            insertCnt = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(exeSql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, sequenceName);
                    ps.setString(2, feature);
                    return ps;
                }
            }, keyHolder);
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .activity("生成序列号")
                    .message("生成序列号{}, 特征{}", sequenceName, feature)
                    .solution("检查当前数据库是否存在" + tableName + ", 请在数据库执行数据库建表语句")
                    .runtimeException();
        }
        if (insertCnt != 1) {
            throw ErrorContextFactory.instance()
                    .activity("生成序列号")
                    .message("生成序列号{}, 特征{} 发生错误", sequenceName, feature)
                    .solution("检查当前数据库是否存在" + tableName + ", 请在数据库执行数据库建表语句")
                    .runtimeException();
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        if (jdbcTemplate == null){
            DataSource dataSource = SpringContextHelper.getBean("defaultDataSource");
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
        String tableName = "sequence_inf";
        String sql = "select seq_value form ";
        if (schema != null && !schema.isEmpty()) {
            sql = sql + schema + ".";
        }
        if (StringUtils.isNotBlank(prefix) && !prefix.isEmpty()){
            tableName = prefix + "_" + tableName;
        }
        sql = sql + tableName + " where seq_name = ? and seq_feature = ? ";
        Integer seqValue = jdbcTemplate.queryForObject(sql, Integer.class);
        return seqValue;
    }
}
