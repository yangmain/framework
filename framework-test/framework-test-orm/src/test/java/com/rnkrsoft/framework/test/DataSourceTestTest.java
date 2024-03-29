package com.rnkrsoft.framework.test;

import com.rnkrsoft.framework.orm.Order;
import com.rnkrsoft.framework.orm.OrderByColumn;
import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.test.dao.OrderDAO;
import com.rnkrsoft.framework.test.dao.UserDAO;
import com.rnkrsoft.framework.test.entity.OrderOrderByEntity;
import com.rnkrsoft.framework.test.entity.UserOrderByEntity;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 */
@CreateTable(entities = {OrderOrderByEntity.class, UserOrderByEntity.class},
        keywordMode = WordMode.lowerCase,
        sqlMode = WordMode.lowerCase,
        prefixMode = NameMode.customize,
        prefix = "xxxx",
        suffixMode = NameMode.customize,
        suffix = "",
        schemaMode = NameMode.customize,
        schema = "",
        createBeforeTest = true,
        dropBeforeCreate = false
)
@ContextConfiguration("classpath*:testContext-orm.xml")
@DataSource(DataSourceType.MySQL)
public class DataSourceTestTest extends DataSourceTest {
    @Test
    public void test1() {
        OrderDAO orderDAO = getBean(OrderDAO.class);
        OrderOrderByEntity entity = new OrderOrderByEntity();
        entity.setAge(1);
        entity.setUserName(UUID.randomUUID().toString());
        entity.setCreateDate(new Date());
        entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
        entity.setStatus(Boolean.TRUE);
        entity.setFlag(1233654L);
        orderDAO.insertSelective(entity);

        entity.addOrderBy(OrderByColumn.builder("age").order(Order.ASC).build());
        entity.addOrderBy(OrderByColumn.builder("user_name").order(Order.DESC).build());
        orderDAO.selectAnd(entity);
//        orderDAO.selectPageAnd(new Pagination<OrderOrderByEntity>(20, 1, entity));
//        entity.setAge(2);
//        orderDAO.selectPageAnd(new Pagination<OrderOrderByEntity>(20, 1, entity));
//        entity.setAge(3);
//        orderDAO.selectPageAnd(new Pagination<OrderOrderByEntity>(20, 1, entity));
//        entity.setAge(4);
//        orderDAO.selectPageAnd(new Pagination<OrderOrderByEntity>(20, 1, entity));
//        entity.setAge(5);
//        orderDAO.selectPageAnd(new Pagination<OrderOrderByEntity>(20, 1, entity));
//        entity.setAge(6);
//        orderDAO.selectPageAnd(new Pagination<OrderOrderByEntity>(20, 1, entity));
        UserDAO userDAO = getBean(UserDAO.class);
        userDAO.countAll();
        UserOrderByEntity userEntity = new UserOrderByEntity();
        userEntity.setAge1(124L);
//        userDAO.insertSelective(userEntity);
//        userDAO.selectAll();
        userDAO.selectAnd(userEntity);
    }
}