package com.rnkrsoft.framework.test.sequence;

import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;
import com.rnkrsoft.framework.sequence.myisam.MyISAMSequenceService;
import com.rnkrsoft.framework.sequence.myisam.entity.SequenceEntity;
import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

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
