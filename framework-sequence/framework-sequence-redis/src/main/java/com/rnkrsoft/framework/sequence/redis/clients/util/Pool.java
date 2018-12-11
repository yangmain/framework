package com.rnkrsoft.framework.sequence.redis.clients.util;

import java.io.Closeable;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.rnkrsoft.framework.sequence.redis.clients.jedis.exceptions.JedisConnectionException;
import com.rnkrsoft.framework.sequence.redis.clients.jedis.exceptions.JedisException;

public abstract class Pool<T> implements Closeable {
  protected GenericObjectPool<T> internalPool;

  /**
   * Using this constructor means you have to set and initialize the internalPool yourself.
   */
  public Pool() {
  }

  public Pool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
    initPool(poolConfig, factory);
  }

  @Override
  public void close() {
    destroy();
  }

  public boolean isClosed() {
    return this.internalPool.isClosed();
  }
  
  public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {

    if (this.internalPool != null) {
      try {
        closeInternalPool();
      } catch (Exception e) {
      }
    }

    this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
  }

  public T getResource() {
    try {
      return internalPool.borrowObject();
    } catch (NoSuchElementException nse) {
      throw new JedisException("Could not get a resource from the pool", nse);
    } catch (Exception e) {
      throw new JedisConnectionException("Could not get a resource from the pool", e);
    }
  }

  /**
   * @deprecated starting from Jedis 3.0 this method will not be exposed. Resource cleanup should be
   *             done using @see {@link com.rnkrsoft.framework.sequence.redis.clients.jedis.Jedis#close()}
   */
  @Deprecated
  public void returnResourceObject(final T resource) {
    if (resource == null) {
      return;
    }
    try {
      internalPool.returnObject(resource);
    } catch (Exception e) {
      throw new JedisException("Could not return the resource to the pool", e);
    }
  }

  /**
   * @deprecated starting from Jedis 3.0 this method will not be exposed. Resource cleanup should be
   *             done using @see {@link com.rnkrsoft.framework.sequence.redis.clients.jedis.Jedis#close()}
   */
  @Deprecated
  public void returnBrokenResource(final T resource) {
    if (resource != null) {
      returnBrokenResourceObject(resource);
    }
  }

  /**
   * @deprecated starting from Jedis 3.0 this method will not be exposed. Resource cleanup should be
   *             done using @see {@link com.rnkrsoft.framework.sequence.redis.clients.jedis.Jedis#close()}
   */
  @Deprecated
  public void returnResource(final T resource) {
    if (resource != null) {
      returnResourceObject(resource);
    }
  }

  public void destroy() {
    closeInternalPool();
  }

  protected void returnBrokenResourceObject(final T resource) {
    try {
      internalPool.invalidateObject(resource);
    } catch (Exception e) {
      throw new JedisException("Could not return the broken resource to the pool", e);
    }
  }

  protected void closeInternalPool() {
    try {
      internalPool.close();
    } catch (Exception e) {
      throw new JedisException("Could not destroy the pool", e);
    }
  }
  
  /**
   * Returns the number of instances currently borrowed from this pool.
   *
   * @return The number of instances currently borrowed from this pool, -1 if
   * the pool is inactive.
   */
  public int getNumActive() {
    if (poolInactive()) {
      return -1;
    }

    return this.internalPool.getNumActive();
  }
  
  /**
   * Returns the number of instances currently idle in this pool.
   *
   * @return The number of instances currently idle in this pool, -1 if the
   * pool is inactive.
   */
  public int getNumIdle() {
    if (poolInactive()) {
      return -1;
    }

    return this.internalPool.getNumIdle();
  }
  
  /**
   * Returns an estimate of the number of threads currently blocked waiting for
   * a resource from this pool.
   *
   * @return The number of threads waiting, -1 if the pool is inactive.
   */
  public int getNumWaiters() {
    if (poolInactive()) {
      return -1;
    }

    return this.internalPool.getNumWaiters();
  }
  
  /**
   * Returns the mean waiting time spent by threads to obtain a resource from
   * this pool.
   *
   * @return The mean waiting time, in milliseconds, -1 if the pool is
   * inactive.
   */
  public long getMeanBorrowWaitTimeMillis() {
    if (poolInactive()) {
      return -1;
    }

    return this.internalPool.getMeanBorrowWaitTimeMillis();
  }
  
  /**
   * Returns the maximum waiting time spent by threads to obtain a resource
   * from this pool.
   *
   * @return The maximum waiting time, in milliseconds, -1 if the pool is
   * inactive.
   */
  public long getMaxBorrowWaitTimeMillis() {
    if (poolInactive()) {
      return -1;
    }

    return this.internalPool.getMaxBorrowWaitTimeMillis();
  }

  private boolean poolInactive() {
    return this.internalPool == null || this.internalPool.isClosed();
  }

  public void addObjects(int count) {
    try {
      for (int i = 0; i < count; i++) {
        this.internalPool.addObject();
      }
    } catch (Exception e) {
      throw new JedisException("Error trying to add idle objects", e);
    }
  }
}
