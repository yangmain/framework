package com.rnkrsoft.framework.test.sequence;

import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 */
public class MyISAMSequenceServiceTest{
    @Test
    public void test2() throws IOException {
        SqlScriptUtils.generate("./target/sql.sql", NameMode.entity, "", NameMode.entity, "", NameMode.entity, "", DataEngineType.MyISAM, WordMode.lowerCase, WordMode.lowerCase, true, true, "com.rnkrsoft.framework.sequence.myisam");
    }
}
