//package com.rnkrsoft.framework.toolkit.jdbc;
//
//import com.devops4j.io.buffer.ByteBuf;
//import com.rnkrsoft.framework.orm.metadata.TableMetadata;
//import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
//import com.rnkrsoft.framework.toolkit.generator.Generator;
//import com.rnkrsoft.framework.toolkit.generator.jdk.JdkEntityGenerator;
//import com.rnkrsoft.framework.toolkit.generator.jdk.JdkMapperGenerator;
//import org.junit.Test;
//
//import java.util.List;
//
///**
// * Created by woate on 2018/4/27.
// */
//public class JdbcReverseMySQLTest {
//
//    @Test
//    public void testReverses() throws Exception {
//        JdbcReverse jdbcReverse = new JdbcReverseMySQL();
//        List<TableMetadata> metadatas = jdbcReverse.reverses("122.114.65.131:3306", "dudule", "root", "duduledmm@2018", "com.rnkrsoft.test", "TB", "INFO");
//        System.out.println(metadatas.get(0));
//
////        for (TableMetadata tableMetadata : metadatas){
//            Generator generator = new JdkMapperGenerator();
//            ByteBuf buf = generator.generate(GenerateContext.builder().tableMetadata(metadatas.get(0)).packageName("com.test1").build());
//            System.out.println(buf.asString("UTF-8"));
////        }
//    }
//}