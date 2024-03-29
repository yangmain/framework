package com.rnkrsoft.framework.sequence.redis.clients.jedis;

import com.rnkrsoft.framework.sequence.redis.clients.util.SafeEncoder;

public enum GeoUnit {
  M, KM, MI, FT;

  public final byte[] raw;

  GeoUnit() {
    raw = SafeEncoder.encode(this.name().toLowerCase());
  }
}
