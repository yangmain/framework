package com.rnkrsoft.framework.sequence.redis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/12/11.
 */
public class RedisSequenceServiceTest {

    @Test
    public void testNextval() throws Exception {

    }

    @Test
    public void testCurval() throws Exception {
        RedisSequenceService redisSequenceService = new RedisSequenceService();
        redisSequenceService.setDatabase(1);
        redisSequenceService.setHost("10.6.6.250");
        redisSequenceService.setPort(6379);
        redisSequenceService.setTimeout(60);
        redisSequenceService.setPassword("pre_123456");
        for (int i = 0; i < 1000; i++) {
            long sequence = redisSequenceService.nextval("", "", "xxx", "20181201");
            System.out.println(sequence);
        }
    }
}