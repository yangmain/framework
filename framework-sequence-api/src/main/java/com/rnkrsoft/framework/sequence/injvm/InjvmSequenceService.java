package com.rnkrsoft.framework.sequence.injvm;

import com.rnkrsoft.framework.sequence.SequenceService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 基于JVM的序号服务
 */
public class InjvmSequenceService implements SequenceService{
    static Map<String, AtomicInteger> SEQUENCES = new ConcurrentHashMap<String, AtomicInteger>();
    @Override
    public int nextval(String schema, String prefix, String sequenceName, String feature) {
        String key = schema + "_" + prefix + "_" + sequenceName + "_" + feature;
        AtomicInteger seq = SEQUENCES.get(key);
        if(seq == null){
            synchronized (SEQUENCES){
                seq = SEQUENCES.get(key);
                if(seq == null){
                    seq = new AtomicInteger(0);
                    SEQUENCES.put(key, seq);
                }
            }
        }
        return seq.incrementAndGet();
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        String key = schema + "_" + prefix + "_" + sequenceName + "_" + feature;
        AtomicInteger seq = SEQUENCES.get(key);
        if(seq == null){
            synchronized (SEQUENCES){
                seq = SEQUENCES.get(key);
                if(seq == null){
                    seq = new AtomicInteger(0);
                    SEQUENCES.put(key, seq);
                }
            }
        }
        return seq.get();
    }
}
