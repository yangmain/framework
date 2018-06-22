package com.rnkrsoft.framework.orm.mybatis.spring.mapper;

import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.jdbc.count.JdbcCountAllMapper;
import com.rnkrsoft.framework.orm.jdbc.count.JdbcCountAndMapper;
import com.rnkrsoft.framework.orm.jdbc.count.JdbcCountOrMapper;
import com.rnkrsoft.framework.orm.jdbc.delete.JdbcDeleteAndMapper;
import com.rnkrsoft.framework.orm.jdbc.delete.JdbcDeleteByPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.jdbc.delete.JdbcDeleteOrMapper;
import com.rnkrsoft.framework.orm.jdbc.delete.JdbcTruncateMapper;
import com.rnkrsoft.framework.orm.jdbc.insert.JdbcInsertAllMapper;
import com.rnkrsoft.framework.orm.jdbc.insert.JdbcInsertSelectiveMapper;
import com.rnkrsoft.framework.orm.jdbc.lock.JdbcLockByForUpdateAndMapper;
import com.rnkrsoft.framework.orm.jdbc.lock.JdbcLockByForUpdateByPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.jdbc.lock.JdbcLockByForUpdateOrMapper;
import com.rnkrsoft.framework.orm.jdbc.lock.JdbcLockByUpdateSetPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.jdbc.select.*;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.count.CountAllMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.count.CountAndMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.count.CountOrMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.DeleteAndMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.DeleteByPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.DeleteOrMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.TruncateMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.insert.InsertMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.insert.InsertSelectiveMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByForUpdateAndMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByForUpdateByPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByForUpdateOrMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByUpdateSetPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.select.*;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.update.UpdateByPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.update.UpdateByPrimaryKeySelectiveMappedStatementBuilder;
import com.rnkrsoft.framework.sequence.spring.SequenceServiceConfigure;
import com.rnkrsoft.framework.orm.jdbc.update.JdbcUpdateByPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.jdbc.update.JdbcUpdateByPrimaryKeySelectiveMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 映射语句注册器
 */
@Slf4j
public abstract class OrmMappedStatementRegister {
    static final List<Class> DAO_INTERFACE_CACHE = new ArrayList();
    static final Set<String> HAVE_LOADED_MAPPER_ID_SET = new ConcurrentSkipListSet();

    /**
     * 增加DAO接口
     *
     * @param daoInterface
     */
    static void addDaoInterface(Class daoInterface) {
        DAO_INTERFACE_CACHE.add(daoInterface);
    }


    /**
     * 清理
     *
     * @param configuration 配置
     */
    public static void clean(Configuration configuration) {
        Map<String, MappedStatement> mappedStatements = null;
        try {
            Class configurationClass = Configuration.class;
            Field mappedStatementsField = configurationClass.getDeclaredField("mappedStatements");
            mappedStatementsField.setAccessible(true);
            mappedStatements = (Map<String, MappedStatement>) mappedStatementsField.get(configuration);
        } catch (Exception e) {
            log.error("清理ORM已映射的自动生成", e);
            return;
        }

        for (String mapperId : HAVE_LOADED_MAPPER_ID_SET) {
            mappedStatements.remove(mapperId);
        }
    }

    /**
     * @param configuration
     * @param ormConfig
     * @param ssc
     * @param daoInterface
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void scan(Configuration configuration, OrmConfig ormConfig, SequenceServiceConfigure ssc, Class daoInterface){
        //如果是开发环境则将DAO接口缓存起来，等待后续处理，如果是生产环境则直接处理
        if (ormConfig.isAllowReload()) {
            addDaoInterface(daoInterface);
        } else {
            scan0(configuration, ormConfig, ssc, daoInterface);
        }
    }

    /**
     * 重新扫描自动生成的Mapper
     *
     * @param configuration 配置对象
     * @param ormConfig     配置对象
     */
    public static void rescan(Configuration configuration,
                              OrmConfig ormConfig,
                              SequenceServiceConfigure ssc) {
        clean(configuration);
        for (Class daoInterface : DAO_INTERFACE_CACHE) {
            scan0(configuration, ormConfig, ssc, daoInterface);
        }
    }

    /**
     * 扫描DAO接口实现的Mapper接口
     *
     * @param configuration 配置对象
     * @param ormConfig     配置
     * @param ssc           序号配置对象
     * @param daoInterface  DAO接口
     */
    static void scan0(Configuration configuration,
                      OrmConfig ormConfig,
                      SequenceServiceConfigure ssc,
                      Class daoInterface
    ) {
        List<MappedStatementBuilder> builders = new ArrayList();
        //---------------------------新增-----------------------------------------
        if (JdbcInsertSelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertSelectiveMappedStatementBuilder(configuration, ormConfig, daoInterface, ssc));
        }
        if (JdbcInsertAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertMappedStatementBuilder(configuration, ormConfig, daoInterface, ssc));
        }
        //---------------------------删除-----------------------------------------
        if (JdbcDeleteAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteAndMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcDeleteOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteOrMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcDeleteByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteByPrimaryKeyMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcTruncateMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new TruncateMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        //---------------------------修改-----------------------------------------
        if (JdbcUpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeyMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcUpdateByPrimaryKeySelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeySelectiveMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        //---------------------------查询-----------------------------------------
        if (JdbcSelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcSelectOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcSelectByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectByPrimaryKeyMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcSelectAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAllMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcSelectPageAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectPageAndMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcSelectPageOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectPageOrMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        //---------------------------统计-----------------------------------------
        if (JdbcCountAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAllMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcCountAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAndMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcCountOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountOrMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        //---------------------------加锁-----------------------------------------
        if (JdbcLockByForUpdateAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateAndMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcLockByForUpdateOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateOrMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcLockByForUpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateByPrimaryKeyMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }
        if (JdbcLockByUpdateSetPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByUpdateSetPrimaryKeyMappedStatementBuilder(configuration, ormConfig, daoInterface));
        }

        for (MappedStatementBuilder builder : builders) {
            MappedStatement ms = builder.build();
            String id = ms.getId();
            if (configuration.hasStatement(id)) {
                log.error("Mybatis has existed MappedStatement id:{},but now override...", id);
            } else {
                configuration.addMappedStatement(ms);
            }
        }
    }
}
