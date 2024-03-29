package com.rnkrsoft.framework.test.service.impl;

import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.Order;
import com.rnkrsoft.framework.orm.OrderByColumn;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.framework.test.dao.OrderDAO;
import com.rnkrsoft.framework.test.dao.UserDAO;
import com.rnkrsoft.framework.test.entity.OrderOrderByEntity;
import com.rnkrsoft.framework.test.entity.UserOrderByEntity;
import com.rnkrsoft.framework.test.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by rnkrsoft.com on 2018/4/26.
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


    @CreateTable(entities = {UserOrderByEntity.class, OrderOrderByEntity.class},
            keywordMode = WordMode.lowerCase,
            sqlMode = WordMode.lowerCase,
            prefixMode = NameMode.customize,
            prefix = "xxxx",
            suffixMode = NameMode.customize,
            suffix = "yyyy",
            schemaMode = NameMode.customize,
            schema = "",
            dropBeforeCreate = false,
            dropAfterTest = false
    )
    @Test
    public void testRegister_error() throws Exception {
        UserService userService = getBean(UserService.class);
        UserDAO userDAO = getBean(UserDAO.class);
        OrderDAO orderDAO = getBean(OrderDAO.class);
        try {
//            userDAO.selectAll();
//            orderDAO.selectAll();

            UserOrderByEntity userEntity =  new UserOrderByEntity();
            userEntity.setUserName("xxx");
            userEntity.addOrderBy(OrderByColumn.builder("user_name", Order.ASC).build());
            userEntity.addOrderBy(OrderByColumn.builder("create_date", Order.ASC).build());
            userDAO.selectAnd(userEntity);
//            String userNo = userService.register("!@#$%", 21);
//            System.out.println(userNo);
//            Assert.assertEquals(36, userNo.length());

//            Assert.fail("不该到这里");
        }catch (Exception e){
            Assert.assertEquals("无效用户名", e.getMessage());
        }

    }
}