package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.mongo.proxy.OperateLogEntity;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import org.junit.Test;

/**
 * Created by woate on 2018/6/21.
 */
public class EntityUtilsTest {

    @Test
    public void testExtractTable() throws Exception {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(OperateLogEntity.class);
        System.out.println(tableMetadata);
    }
}