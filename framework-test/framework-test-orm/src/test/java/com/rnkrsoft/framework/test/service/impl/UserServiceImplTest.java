package com.rnkrsoft.framework.test.service.impl;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.framework.test.dao.OrmDemoDAO;
import com.rnkrsoft.framework.test.dao.UserInfoDAO;
import com.rnkrsoft.framework.test.entity.OrmEntity;
import com.rnkrsoft.framework.test.entity.UserInfoEntity;
import com.rnkrsoft.framework.test.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/4/26.
 */
@ContextConfiguration("classpath*:testContext-orm.xml")
@DataSource(DataSourceType.H2)
public class UserServiceImplTest extends DataSourceTest {
    @Test
    public void testRegister() throws Exception {
        UserService userService = getBean(UserService.class);
        String userNo = userService.register("!@#$", 21);
        System.out.println(userNo);
        Assert.assertEquals(36, userNo.length());
    }


    @CreateTable(entities = {UserInfoEntity.class, OrmEntity.class},
            keywordMode = WordMode.lowerCase,
            sqlMode = WordMode.lowerCase,
            prefixMode = NameMode.customize,
            prefix = "xxxx",
            suffixMode = NameMode.customize,
            suffix = "yyyy",
            schemaMode = NameMode.customize,
            schema = "xxxxxx",
            dropBeforeCreate = false
    )
    @Test
    public void testRegister_error() throws Exception {
        UserService userService = getBean(UserService.class);
        UserInfoDAO userInfoDAO = getBean(UserInfoDAO.class);
        OrmDemoDAO ormDemoDAO = getBean(OrmDemoDAO.class);
        try {
            userInfoDAO.selectAll();
            ormDemoDAO.selectAll();
            String userNo = userService.register("!@#$%", 21);
            System.out.println(userNo);
//            Assert.assertEquals(36, userNo.length());

            Assert.fail("不该到这里");
        }catch (Exception e){
            Assert.assertEquals("无效用户名", e.getMessage());
        }

    }
}