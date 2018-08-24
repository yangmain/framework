package com.rnkrsoft.framework.orm.extractor;

import com.rnkrsoft.framework.orm.entity.JpaDemoEntity;
import com.rnkrsoft.framework.orm.entity.OrmDemoEntity;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/6/25.
 */
public class OrmOrderByEntityExtractorTest {

    @Test
    public void testExtractTable() throws Exception {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(OrmDemoEntity.class, false);
        for (String column : tableMetadata.getOrderColumns()){
            System.out.println(tableMetadata.getColumn(column));
        }
    }
}