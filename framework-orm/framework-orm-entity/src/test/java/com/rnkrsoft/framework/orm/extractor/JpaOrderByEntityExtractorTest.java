package com.rnkrsoft.framework.orm.extractor;

import com.rnkrsoft.framework.orm.entity.JpaDemoEntity;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/4/9.
 */
public class JpaOrderByEntityExtractorTest {

    @Test
    public void testExtractTable() throws Exception {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(JpaDemoEntity.class, false);
        for (String column : tableMetadata.getOrderColumns()){
            System.out.println(tableMetadata.getColumn(column));
        }
    }
}