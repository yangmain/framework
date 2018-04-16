package com.rnkrsoft.framework.sequence.jnjvm;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 */
public class InjvmSequenceServiceTest {

    @Test
    public void testNextval() throws Exception {
        SequenceService sequenceService = SequenceServiceFactory.instance();
        System.out.println(sequenceService.nextval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.nextval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.nextval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.nextval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.nextval(null, "PRE", "seqName", null));
    }

    @Test
    public void testCurval() throws Exception {
        SequenceService sequenceService = SequenceServiceFactory.instance();
        System.out.println(sequenceService.curval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.nextval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.curval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.curval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.curval(null, "PRE", "seqName", null));
        System.out.println(sequenceService.curval(null, "PRE", "seqName", null));
    }
}