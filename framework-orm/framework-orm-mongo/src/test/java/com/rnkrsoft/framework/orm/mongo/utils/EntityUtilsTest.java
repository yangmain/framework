package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.orm.mongo.example.example1.entity.Example1Entity;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 */
public class EntityUtilsTest {

    @Test
    public void testExtractTable() throws Exception {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(Example1Entity.class);
        System.out.println(tableMetadata);
    }
}