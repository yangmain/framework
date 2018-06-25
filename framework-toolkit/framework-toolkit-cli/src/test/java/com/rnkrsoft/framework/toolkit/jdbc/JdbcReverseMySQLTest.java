package com.rnkrsoft.framework.toolkit.jdbc;

import com.rnkrsoft.framework.toolkit.generator.jdk.JdkDaoGenerator;
import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
import com.rnkrsoft.framework.toolkit.generator.Generator;
import com.rnkrsoft.framework.toolkit.generator.jdk.JdkEntityGenerator;
import com.rnkrsoft.framework.toolkit.generator.jdk.JdkMapperGenerator;
import org.junit.Test;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/27.
 */
public class JdbcReverseMySQLTest {

    @Test
    public void testReverses() throws Exception {
        JdbcReverse jdbcReverse = new JdbcReverseMySQL();
        List<TableMetadata> metadatas = jdbcReverse.reverses("192.168.0.111:3333", "ccclubs_yun_sys", "root", "root", "com.zxevpop",new String[] {"TB", "SRV"}, null);
//        List<TableMetadata> metadatas = jdbcReverse.reverses("192.168.0.111:3333", "ccclubs_yun_sys", "root", "root", "com.zxevpop", new String []{"srv"}, new String []{"LOG"});

        for (TableMetadata tableMetadata : metadatas){
            if (!tableMetadata.getTableName().equalsIgnoreCase("API")){
                continue;
            }
            Generator generator = new JdkEntityGenerator();
            ByteBuf buf = generator.generate(GenerateContext.builder().tableMetadata(tableMetadata).packageName("com.zxevpop").build());
            System.out.println(buf.asString("UTF-8"));
        }
    }
}