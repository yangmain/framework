package com.rnkrsoft.framework.sequence.redis;

import com.rnkrsoft.framework.sequence.SequenceService;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 */
public class RedisSequenceService implements SequenceService{
    @Override
    public int nextval(String schema, String prefix, String sequenceName, String feature) {
        return 0;
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        return 0;
    }
}
