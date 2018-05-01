//package com.rnkrsoft.framework.test.sequence;
//
//import com.devops4j.utils.DateUtils;
//import com.rnkrsoft.framework.orm.NameMode;
//import com.rnkrsoft.framework.orm.WordMode;
//import com.rnkrsoft.framework.sequence.SequenceService;
//import com.rnkrsoft.framework.sequence.SequenceServiceFactory;
//import com.rnkrsoft.framework.sequence.myisam.MyISAMSequenceService;
//import com.rnkrsoft.framework.sequence.myisam.entity.SequenceEntity;
//import com.rnkrsoft.framework.test.CreateTable;
//import com.rnkrsoft.framework.test.DataSource;
//import com.rnkrsoft.framework.test.DataSourceTest;
//import com.rnkrsoft.framework.test.DataSourceType;
//import com.rnkrsoft.framework.test.entity.OrmEntity;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.test.context.ContextConfiguration;
//
///**
// * Created by woate on 2018/4/23.
// */
//@ContextConfiguration("classpath*:testContext-orm.xml")
//@DataSource(DataSourceType.H2)
//public class MyISAMSequenceServiceTest  extends DataSourceTest {
//    @CreateTable(entities = {OrmEntity.class, SequenceEntity.class},
//            keywordMode = WordMode.lowerCase,
//            sqlMode = WordMode.lowerCase,
//            prefixMode = NameMode.auto,
//            suffixMode = NameMode.auto,
//            schemaMode = NameMode.auto,
//            testBeforeDrop = false
//    )
//    @Test
//    public void test1(){
//        SequenceService sequenceService = SequenceServiceFactory.instance(MyISAMSequenceService.class.getName());
//        int x1 = sequenceService.nextval("", "", "Test", DateUtils.getCurrDate());
//        Assert.assertEquals(1, x1);
//        int x2 = sequenceService.nextval("", "", "Test", DateUtils.getCurrDate());
//        Assert.assertEquals(2, x2);
//    }
//}
