package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.dao.OperateLogEntity;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/6/22.
 */
public class OperateLogEntityTest {

    @Test
    public void testGetId() throws Exception {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(OperateLogEntity.class);
        System.out.println(tableMetadata);
    }
}