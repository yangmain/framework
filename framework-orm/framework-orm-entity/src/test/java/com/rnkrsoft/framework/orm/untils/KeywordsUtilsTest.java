package com.rnkrsoft.framework.orm.untils;

import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/4/6.
 */
public class KeywordsUtilsTest {

    @Test
    public void testValidateKeyword() throws Exception {
        ColumnMetadata columnMetadata = new ColumnMetadata();
        columnMetadata.setJdbcName("NAME");
        columnMetadata.setJavaName("name");
        KeywordsUtils.validateKeyword(columnMetadata);

    }

    @Test
    public void testConvert() throws Exception {
        String sql = KeywordsUtils.convert("SELECT", WordMode.lowerCase);
        System.out.println(sql);
    }
}