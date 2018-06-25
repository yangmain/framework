package com.rnkrsoft.framework.sequence;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 */
public class SequenceServiceFactoryTest {

    @Test
    public void testInstance() throws Exception {
        SequenceService sequenceService = SequenceServiceFactory.instance();
        System.out.println(sequenceService.nextval("", "xx","SEQ1" , ""));
    }
}