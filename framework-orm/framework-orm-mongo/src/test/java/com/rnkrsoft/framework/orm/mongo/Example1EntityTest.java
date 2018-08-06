package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.example.example1.entity.Example1Entity;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/6/22.
 */
public class Example1EntityTest {

    @Test
    public void testGetId() throws Exception {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(Example1Entity.class);
        System.out.println(tableMetadata);
    }
}