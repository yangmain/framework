package com.rnkrsoft.framework.orm.mybatis.spring.mapper;

import com.rnkrsoft.framework.orm.DatabaseType;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.mybatis.plugins.PaginationStage2Interceptor;
import com.rnkrsoft.framework.orm.mybatis.sequence.SequenceServiceConfigure;
import com.rnkrsoft.framework.orm.NameMode;
import lombok.Setter;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.rnkrsoft.framework.orm.mybatis.plugins.PaginationStage1Interceptor;
import com.rnkrsoft.framework.orm.mybatis.spring.OrmSessionDaoSupport;

import java.io.IOException;

import static org.springframework.util.Assert.notNull;

public class OrmMapperFactoryBean<T> extends OrmSessionDaoSupport implements FactoryBean<T> {
    @Setter
    Class<T> mapperInterface;
    @Setter
    OrmConfig ormConfig;
    /**
     * 序号配置
     */
    @Setter
    @Autowired(required = false)
    SequenceServiceConfigure sequenceServiceConfigure;

    public OrmMapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public OrmMapperFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        notNull(this.ormConfig, "Property 'ormConfig' is required");
        super.checkDaoConfig();
        notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = getSqlSession().getConfiguration();
        //扫描接口
        OrmMappedStatementRegister.scan(configuration, this.ormConfig, sequenceServiceConfigure, this.mapperInterface);
        if (!configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
    }

    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

}
