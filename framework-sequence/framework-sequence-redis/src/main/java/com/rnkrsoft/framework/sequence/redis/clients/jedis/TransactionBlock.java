package com.rnkrsoft.framework.sequence.redis.clients.jedis;

import com.rnkrsoft.framework.sequence.redis.clients.jedis.exceptions.JedisException;

@Deprecated
/**
 * This class is deprecated due to its error prone
 * and will be removed on next major release
 * @see https://github.com/xetorthio/jedis/pull/498
 */
public abstract class TransactionBlock extends Transaction {
  public TransactionBlock(Client client) {
    super(client);
  }

  public TransactionBlock() {
  }

  public abstract void execute() throws JedisException;

  public void setClient(Client client) {
    this.client = client;
  }
}
