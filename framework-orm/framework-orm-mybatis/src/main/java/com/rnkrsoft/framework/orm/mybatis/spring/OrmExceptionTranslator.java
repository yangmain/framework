package com.rnkrsoft.framework.orm.mybatis.spring;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.SQLException;

public class OrmExceptionTranslator implements PersistenceExceptionTranslator {

  private final DataSource dataSource;

  private SQLExceptionTranslator exceptionTranslator;

  public OrmExceptionTranslator(DataSource dataSource, boolean exceptionTranslatorLazyInit) {
    this.dataSource = dataSource;

    if (!exceptionTranslatorLazyInit) {
      this.initExceptionTranslator();
    }
  }

  @Override
  public DataAccessException translateExceptionIfPossible(RuntimeException e) {
    if (e instanceof PersistenceException) {
      // Batch exceptions come inside another PersistenceException
      // recursion has a risk of infinite loop so better make another if
      if (e.getCause() instanceof PersistenceException) {
        e = (PersistenceException) e.getCause();
      }
      if (e.getCause() instanceof SQLException) {
        this.initExceptionTranslator();
        return this.exceptionTranslator.translate(e.getMessage() + "\n", null, (SQLException) e.getCause());
      }
//      return e;
    } 
    return null;
  }

  /**
   * Initializes the internal translator reference.
   */
  private synchronized void initExceptionTranslator() {
    if (this.exceptionTranslator == null) {
      this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
    }
  }

}
