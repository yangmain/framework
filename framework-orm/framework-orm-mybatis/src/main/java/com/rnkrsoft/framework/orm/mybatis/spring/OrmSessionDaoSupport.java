package com.rnkrsoft.framework.orm.mybatis.spring;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.support.DaoSupport;

import static org.springframework.util.Assert.notNull;
/**
 * Created by rnkrsoft on 2018/4/2.
 * DaoSupport集成方式实现
 */
public abstract class OrmSessionDaoSupport extends DaoSupport {

  private SqlSession sqlSession;

  private boolean externalSqlSession;

  public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
    if (!this.externalSqlSession) {
      this.sqlSession = new OrmSessionTemplate(sqlSessionFactory);
    }
  }

  public void setSqlSessionTemplate(OrmSessionTemplate ormSessionTemplate) {
    this.sqlSession = ormSessionTemplate;
    this.externalSqlSession = true;
  }

  public SqlSession getSqlSession() {
    return this.sqlSession;
  }

  @Override
  protected void checkDaoConfig() {
    notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
  }

}
