package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.devops4j.io.buffer.ByteBuf;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
import com.rnkrsoft.framework.toolkit.generator.Generator;
import com.rnkrsoft.framework.toolkit.generator.entity.OrmEntity;
import org.junit.Test;

/**
 * Created by woate on 2018/4/26.
 */
public class JdkDaoGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(OrmEntity.class, true);
        System.out.println(tableMetadata);
        Generator generator = new JdkDaoGenerator();
        ByteBuf bytecode = generator.generate(GenerateContext.builder().packageName("com.test").tableMetadata(tableMetadata).build());
        System.out.println(bytecode.asString("UTF-8"));
    }
}