package com.rnkrsoft.framework.orm.mybatis.spring;

import com.rnkrsoft.framework.orm.mybatis.spring.transaction.SpringManagedTransactionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.springframework.util.Assert.notNull;
@Slf4j
public final class OrmSessionUtils {
  private static final String NO_EXECUTOR_TYPE_SPECIFIED = "No ExecutorType specified";
  private static final String NO_SQL_SESSION_FACTORY_SPECIFIED = "No SqlSessionFactory specified";
  private static final String NO_SQL_SESSION_SPECIFIED = "No SqlSession specified";

  private OrmSessionUtils() {
    // do nothing
  }

  public static SqlSession getSqlSession(SqlSessionFactory sessionFactory) {
    ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
    return getSqlSession(sessionFactory, executorType, null);
  }

  public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {

    notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
    notNull(executorType, NO_EXECUTOR_TYPE_SPECIFIED);

    OrmSessionHolder holder = (OrmSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);

    SqlSession session = sessionHolder(executorType, holder);
    if (session != null) {
      return session;
    }

    if (log.isDebugEnabled()) {
      log.debug("Creating a new SqlSession");
    }

    session = sessionFactory.openSession(executorType);

    registerSessionHolder(sessionFactory, executorType, exceptionTranslator, session);

    return session;
  }

  private static void registerSessionHolder(SqlSessionFactory sessionFactory, ExecutorType executorType,
      PersistenceExceptionTranslator exceptionTranslator, SqlSession session) {
    OrmSessionHolder holder;
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      Environment environment = sessionFactory.getConfiguration().getEnvironment();

      if (environment.getTransactionFactory() instanceof SpringManagedTransactionFactory) {
        if (log.isDebugEnabled()) {
          log.debug("Registering transaction synchronization for SqlSession [" + session + "]");
        }

        holder = new OrmSessionHolder(session, executorType, exceptionTranslator);
        TransactionSynchronizationManager.bindResource(sessionFactory, holder);
        TransactionSynchronizationManager.registerSynchronization(new SqlSessionSynchronization(holder, sessionFactory));
        holder.setSynchronizedWithTransaction(true);
        holder.requested();
      } else {
        if (TransactionSynchronizationManager.getResource(environment.getDataSource()) == null) {
          if (log.isDebugEnabled()) {
            log.debug("SqlSession [" + session + "] was not registered for synchronization because DataSource is not transactional");
          }
        } else {
          throw new TransientDataAccessResourceException(
              "SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
        }
      }
    } else {
      if (log.isDebugEnabled()) {
        log.debug("SqlSession [" + session + "] was not registered for synchronization because synchronization is not active");
      }
    }
}

  private static SqlSession sessionHolder(ExecutorType executorType, OrmSessionHolder holder) {
    SqlSession session = null;
    if (holder != null && holder.isSynchronizedWithTransaction()) {
      if (holder.getExecutorType() != executorType) {
        throw new TransientDataAccessResourceException("Cannot change the ExecutorType when there is an existing transaction");
      }

      holder.requested();

      if (log.isDebugEnabled()) {
        log.debug("Fetched SqlSession [" + holder.getSqlSession() + "] from current transaction");
      }

      session = holder.getSqlSession();
    }
    return session;
  }

  public static void closeSqlSession(SqlSession session, SqlSessionFactory sessionFactory) {
    notNull(session, NO_SQL_SESSION_SPECIFIED);
    notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);

    OrmSessionHolder holder = (OrmSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    if ((holder != null) && (holder.getSqlSession() == session)) {
      if (log.isDebugEnabled()) {
        log.debug("Releasing transactional SqlSession [" + session + "]");
      }
      holder.released();
    } else {
      if (log.isDebugEnabled()) {
        log.debug("Closing non transactional SqlSession [" + session + "]");
      }
      session.close();
    }
  }

  public static boolean isSqlSessionTransactional(SqlSession session, SqlSessionFactory sessionFactory) {
    notNull(session, NO_SQL_SESSION_SPECIFIED);
    notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);

    OrmSessionHolder holder = (OrmSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);

    return (holder != null) && (holder.getSqlSession() == session);
  }

  private static final class SqlSessionSynchronization extends TransactionSynchronizationAdapter {

    private final OrmSessionHolder holder;

    private final SqlSessionFactory sessionFactory;

    private boolean holderActive = true;

    public SqlSessionSynchronization(OrmSessionHolder holder, SqlSessionFactory sessionFactory) {
      notNull(holder, "Parameter 'holder' must be not null");
      notNull(sessionFactory, "Parameter 'sessionFactory' must be not null");

      this.holder = holder;
      this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
      // order right before any Connection synchronization
      return DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void suspend() {
      if (this.holderActive) {
        if (log.isDebugEnabled()) {
          log.debug("Transaction synchronization suspending SqlSession [" + this.holder.getSqlSession() + "]");
        }
        TransactionSynchronizationManager.unbindResource(this.sessionFactory);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
      if (this.holderActive) {
        if (log.isDebugEnabled()) {
          log.debug("Transaction synchronization resuming SqlSession [" + this.holder.getSqlSession() + "]");
        }
        TransactionSynchronizationManager.bindResource(this.sessionFactory, this.holder);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeCommit(boolean readOnly) {
      // Connection commit or rollback will be handled by ConnectionSynchronization or
      // DataSourceTransactionManager.
      // But, do cleanup the SqlSession / Executor, including flushing BATCH statements so
      // they are actually executed.
      // SpringManagedTransaction will no-op the commit over the jdbc connection
      // TODO This updates 2nd level caches but the tx may be rolledback later on! 
      if (TransactionSynchronizationManager.isActualTransactionActive()) {
        try {
          if (log.isDebugEnabled()) {
            log.debug("Transaction synchronization committing SqlSession [" + this.holder.getSqlSession() + "]");
          }
          this.holder.getSqlSession().commit();
        } catch (PersistenceException p) {
          if (this.holder.getPersistenceExceptionTranslator() != null) {
            DataAccessException translated = this.holder
                .getPersistenceExceptionTranslator()
                .translateExceptionIfPossible(p);
            if (translated != null) {
              throw translated;
            }
          }
          throw p;
        }
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeCompletion() {
      // Issue #18 Close SqlSession and deregister it now
      // because afterCompletion may be called from a different thread
      if (!this.holder.isOpen()) {
        if (log.isDebugEnabled()) {
          log.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        this.holderActive = false;
        if (log.isDebugEnabled()) {
          log.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
        }
        this.holder.getSqlSession().close();
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(int status) {
      if (this.holderActive) {
        // afterCompletion may have been called from a different thread
        // so avoid failing if there is nothing in this one
        if (log.isDebugEnabled()) {
          log.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
        }
        TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
        this.holderActive = false;
        if (log.isDebugEnabled()) {
          log.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
        }
        this.holder.getSqlSession().close();
      }
      this.holder.reset();
    }
  }

}
