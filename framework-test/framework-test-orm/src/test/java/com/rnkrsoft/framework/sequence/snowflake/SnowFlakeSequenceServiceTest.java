package com.rnkrsoft.framework.sequence.snowflake;

import com.rnkrsoft.framework.sequence.SequenceService;
import org.junit.Test;

/**
 * Created by woate on 2019/9/20.
 */
public class SnowFlakeSequenceServiceTest {

    @Test
    public void testNextval() throws Exception {
        SequenceService sequenceService = new SnowFlakeSequenceService();
        System.out.println(sequenceService.nextval(null, null, null, null));
    }

    @Test
    public void testCurval() throws Exception {

    }
}