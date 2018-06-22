package com.rnkrsoft.framework.sequence.spring;

import com.rnkrsoft.framework.sequence.SequenceService;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by Administrator on 2018/6/22.
 */
public class ClasspathSequenceServiceConfigureTest {

    @Test
    public void testRegister() throws Exception {
        Properties mappings = new Properties();
        mappings.put("table1", "com.rnkrsoft.framework.sequence.injvm.InjvmSequenceService");
        ClasspathSequenceServiceConfigure sequenceServiceConfigure = new ClasspathSequenceServiceConfigure(mappings);
        SequenceService sequenceService = sequenceServiceConfigure.getSequenceService("table1");
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
        System.out.println(sequenceService.nextval("xxx", "xxx", "SEQ1", ""));
    }
}