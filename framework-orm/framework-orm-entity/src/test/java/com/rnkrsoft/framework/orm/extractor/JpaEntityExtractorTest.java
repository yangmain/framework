package com.rnkrsoft.framework.orm.extractor;

import com.rnkrsoft.framework.orm.entity.JpaDemoEntity;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/4/9.
 */
public class JpaEntityExtractorTest {

    @Test
    public void testExtractTable() throws Exception {
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata tableMetadata = helper.extractTable(JpaDemoEntity.class, false);
        System.out.println(tableMetadata);
    }

    @Test
    public void testExtractFieldString() throws Exception {

    }

    @Test
    public void testExtractFieldNumber() throws Exception {

    }

    @Test
    public void testExtractFieldDate() throws Exception {

    }

    @Test
    public void testExtractFieldPrimaryKey() throws Exception {

    }

    @Test
    public void testExtractField() throws Exception {

    }
}