package com.rnkrsoft.framework.sequence.h2;

import com.rnkrsoft.framework.sequence.SequenceService;

/**
 * Created by rnkrsoft on 2017/1/4.
 * 基于H2的序号服务
 */
public class H2SequenceService implements SequenceService{
    @Override
    public int nextval(String schema, String prefix, String sequenceName, String feature) {
        return 0;
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        return 0;
    }
}
