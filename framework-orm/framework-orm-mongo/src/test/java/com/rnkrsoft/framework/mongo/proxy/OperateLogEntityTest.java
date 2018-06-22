package com.rnkrsoft.framework.mongo.proxy;

import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/6/22.
 */
public class OperateLogEntityTest {

    @Test
    public void testGetId() throws Exception {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(OperateLogEntity.class);
        System.out.println(tableMetadata);
    }
}