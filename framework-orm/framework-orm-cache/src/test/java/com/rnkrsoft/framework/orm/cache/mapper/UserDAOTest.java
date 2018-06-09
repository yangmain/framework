package com.rnkrsoft.framework.orm.cache.mapper;

import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by woate on 2018/6/8.
 */
@ContextConfiguration(locations = "classpath*:testContext-cache.xml")
public class UserDAOTest extends SpringTest {

    @Test
    public void testSet() throws Exception {
        UserDAO userDAO = getBean(UserDAO.class);
        System.out.println(userDAO.keys("*"));
        String key = UUID.randomUUID().toString();
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.keys("*"));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.keys("*"));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
    }
}