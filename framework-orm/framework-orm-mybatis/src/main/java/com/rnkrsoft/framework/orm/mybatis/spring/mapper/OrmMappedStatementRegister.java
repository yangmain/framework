package com.rnkrsoft.framework.orm.mybatis.spring.mapper;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.reflection4j.GlobalSystemMetadata;
import com.devops4j.reflection4j.MetaObject;
import com.devops4j.reflection4j.Reflector;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.count.CountAllMapper;
import com.rnkrsoft.framework.orm.count.CountAndMapper;
import com.rnkrsoft.framework.orm.count.CountOrMapper;
import com.rnkrsoft.framework.orm.delete.DeleteAndMapper;
import com.rnkrsoft.framework.orm.delete.DeleteByPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.delete.DeleteOrMapper;
import com.rnkrsoft.framework.orm.delete.TruncateMapper;
import com.rnkrsoft.framework.orm.insert.InsertAllMapper;
import com.rnkrsoft.framework.orm.insert.InsertSelectiveMapper;
import com.rnkrsoft.framework.orm.lock.LockByForUpdateAndMapper;
import com.rnkrsoft.framework.orm.lock.LockByForUpdateByPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.lock.LockByForUpdateOrMapper;
import com.rnkrsoft.framework.orm.lock.LockByUpdateSetPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.count.CountOrMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.DeleteByPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.TruncateMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByUpdateSetPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.select.*;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.update.UpdateByPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.update.UpdateByPrimaryKeySelectiveMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.DeleteAndMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete.DeleteOrMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.insert.InsertMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.insert.InsertSelectiveMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByForUpdateAndMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByForUpdateByPrimaryKeyMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock.LockByForUpdateOrMappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.sequence.SequenceServiceConfigure;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.count.CountAndMappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.*;
import com.rnkrsoft.framework.orm.update.UpdateByPrimaryKeyMapper;
import com.rnkrsoft.framework.orm.update.UpdateByPrimaryKeySelectiveMapper;
import com.rnkrsoft.framework.test.TableNameMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.count.CountAllMappedStatementBuilder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 映射语句注册器
 */
@Slf4j
public abstract class OrmMappedStatementRegister {
    /**
     * MAPPER建造器缓存
     */
    static final Set<MappedStatementBuilder> MAPPER_BUILDER_CACHE = new HashSet();
    /**
     * 已经加载过的Mapper编号集合
     */
    static final Set<String> HAVE_LOADED_MAPPER_ID_SET = new HashSet();
    /**
     * 重新扫描自动生成的Mapper
     * @param configuration 配置对象
     * @param sqlMode SQL模式
     * @param keywordMode 关键字模式
     * @param schemaMode 数据库模式类型
     * @param schema 数据库模式
     * @param prefixMode 前缀模式
     * @param prefix 前缀
     * @param suffixMode 后缀模式
     * @param suffix 后缀
     */
    public static void rescan(Configuration configuration,
                              WordMode sqlMode,
                              WordMode keywordMode,
                              TableNameMode schemaMode,
                              String schema,
                              TableNameMode prefixMode,
                              String prefix,
                              TableNameMode suffixMode,
                              String suffix) {
        Reflector reflector = GlobalSystemMetadata.reflector(Configuration.class);
        Map<String, MappedStatement> mappedStatements = null;
        try {
            Field mappedStatementsField = reflector.getField("mappedStatements");
            mappedStatements = (Map) mappedStatementsField.get(configuration);
        } catch (Exception e) {
            throw ErrorContextFactory.instance().message("获取Configuration.mappedStatements 发生错误").cause(e).runtimeException();
        }
        //移除已经加载的MAPPER编号的语句对象
        for (String id : HAVE_LOADED_MAPPER_ID_SET){
            mappedStatements.remove(id);
        }
        for (MappedStatementBuilder builder : MAPPER_BUILDER_CACHE) {
            builder.setKeywordMode(keywordMode);
            builder.setSqlMode(sqlMode);
            builder.setSchemaMode(schemaMode);
            builder.setSchema(schema);
            builder.setPrefixMode(prefixMode);
            builder.setPrefix(prefix);
            builder.setSuffixModed(suffixMode);
            builder.setSuffix(suffix);
            MappedStatement ms = builder.build();
            String id = ms.getId();
            if (configuration.hasStatement(id)) {
                log.error("ORM存在Mapper id:{0},正好在进行覆盖...", id);
            } else {
                configuration.addMappedStatement(ms);
            }
        }
    }

    /**
     * 通过Junit识别运行环境
     * @return
     */
    static boolean isTestEnv() {
        try {
            Class.forName("org.junit.Test");
            return true;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("junit.framework.Test");
                return true;
            } catch (ClassNotFoundException e1) {
                return false;
            }
        }
    }

    /**
     * 扫描DAO接口实现的Mapper接口
     * @param configuration 配置对象
     * @param daoInterface DAO接口
     * @param sqlMode SQL模式
     * @param keywordMode 关键字模式
     * @param schemaMode 数据库模式类型
     * @param schema 数据库模式
     * @param prefixMode 前缀模式
     * @param prefix 前缀
     * @param suffixMode 后缀模式
     * @param suffix 后缀
     * @param strict 是否严格注解
     * @param ssc 序号配置对象
     */
    public static void scan(Configuration configuration,
                      Class daoInterface,
                      WordMode sqlMode,
                      WordMode keywordMode,
                      TableNameMode schemaMode,
                      String schema,
                      TableNameMode prefixMode,
                      String prefix,
                      TableNameMode suffixMode,
                      String suffix,
                      boolean strict,
                      SequenceServiceConfigure ssc) {
        List<MappedStatementBuilder> builders = new ArrayList();
        //---------------------------新增-----------------------------------------
        if (InsertSelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertSelectiveMappedStatementBuilder(configuration, daoInterface, ssc));
        }
        if (InsertAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertMappedStatementBuilder(configuration, daoInterface, ssc));
        }
        //---------------------------删除-----------------------------------------
        if (DeleteAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (DeleteOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteOrMappedStatementBuilder(configuration, daoInterface));
        }
        if (DeleteByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (TruncateMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new TruncateMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------修改-----------------------------------------
        if (UpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (UpdateByPrimaryKeySelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeySelectiveMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------查询-----------------------------------------
        if (SelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAllMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectPageAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectPageAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectPageOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------统计-----------------------------------------
        if (CountAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAllMappedStatementBuilder(configuration, daoInterface));
        }
        if (CountAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (CountOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountOrMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------加锁-----------------------------------------
        if (LockByForUpdateAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (LockByForUpdateOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateOrMappedStatementBuilder(configuration, daoInterface));
        }
        if (LockByForUpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (LockByUpdateSetPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByUpdateSetPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        for (MappedStatementBuilder builder : builders) {
            builder.setKeywordMode(keywordMode);
            builder.setSqlMode(sqlMode);
            builder.setSchemaMode(schemaMode);
            builder.setSchema(schema);
            builder.setPrefixMode(prefixMode);
            builder.setPrefix(prefix);
            builder.setSuffixModed(suffixMode);
            builder.setSuffix(suffix);
            builder.setStrict(strict);
            MappedStatement ms = builder.build();
            String id = ms.getId();
            if (configuration.hasStatement(id)) {
                log.error("Mybatis has existed MappedStatement id:{0},but now override...", id);
            } else {
                configuration.addMappedStatement(ms);
                HAVE_LOADED_MAPPER_ID_SET.add(ms.getId());
                MAPPER_BUILDER_CACHE.add(builder);
            }
        }
    }
}
