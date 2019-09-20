package com.rnkrsoft.framework.sequence.snowflake;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.id.SnowFlakeIdGenerator;

/**
 * Created by rnkrsofr.com on 2019/9/20.
 * 基于雪花算法实现的序号服务
 */
public class SnowFlakeSequenceService implements SequenceService {
    @Override
    public long nextval(String schema, String prefix, String sequenceName, String feature) {
        return SnowFlakeIdGenerator.getInstance().next();
    }

    @Override
    public long curval(String schema, String prefix, String sequenceName, String feature) {
        System.err.println("SnowFlake not supported curval()!");
        return -1;
    }
}
