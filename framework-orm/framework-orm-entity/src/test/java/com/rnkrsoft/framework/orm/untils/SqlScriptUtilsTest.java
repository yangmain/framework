package com.rnkrsoft.framework.orm.untils;

import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/4.
 */
public class SqlScriptUtilsTest {

    @Test
    public void testGenerate() throws Exception {
        SqlScriptUtils.generate("./target/sql.sql", NameMode.entity, "", NameMode.entity, "", NameMode.entity, "", DataEngineType.AUTO, WordMode.lowerCase, WordMode.lowerCase,  true, true, "com.rnkrsoft.framework");
    }
}