package com.rnkrsoft.framework.orm.cache;

import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.orm.cache.entity.User;
import com.rnkrsoft.framework.orm.cache.mapper.UserDAO;
import com.rnkrsoft.framework.test.SpringTest;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/8.
 */
@ContextConfiguration(locations = "classpath*:testContext-cache.xml")
public class UserDAOTest extends SpringTest {
    @Autowired
    UserDAO userDAO;

    @Test
    public void testSet() throws Exception {
        userDAO.get().remove("1");
        userDAO.get().remove("2");
        userDAO.get().remove("3");
        userDAO.get().remove("4");
        userDAO.get().remove("5");
        userDAO.set("1", new User(UUID.randomUUID().toString(), 1000, false));
        userDAO.expire("1", 2);
        Long l1 = userDAO.expire1("1", 2);
        Assert.assertEquals(1, l1.longValue());
        Thread.sleep(3 * 1000);
        Assert.assertEquals(1L, userDAO.incr("2").longValue());
        Assert.assertEquals(0L, userDAO.decr("2").longValue());
        Assert.assertEquals(0L, userDAO.get1("2").longValue());
        userDAO.set("3", new User(UUID.randomUUID().toString(), 1024, false));
        User temp = userDAO.set1("3", new User(UUID.randomUUID().toString(), 1000, false));
        Assert.assertEquals(1024,temp.getAge());
        userDAO.set2("4", Long.valueOf(1));
        Long temp2 = userDAO.set3("4", Long.valueOf(1));
        Assert.assertEquals(1,temp2.longValue());
        System.out.println(temp2);
        userDAO.set4("5", "3");
        String temp3 = userDAO.set5("5", "4");
        Assert.assertEquals("3", temp3);
        CachedMap cachedMap = userDAO.get();
        System.out.println(cachedMap.keys("*"));
        User user = userDAO.getSet("yyyyyy", new User(UUID.randomUUID().toString(), 1000, false));
        System.out.println(user);
        System.out.println(userDAO.type("yyyyyy"));
        System.out.println(userDAO.get("yyyyyy"));
        userDAO.expire("yyyyyy", 2);
        System.out.println(userDAO.ttl("yyyyyy"));
        userDAO.persist("yyyyyy");
        Thread.sleep(3000);
        System.out.println(userDAO.ttl("yyyyyy"));
        System.out.println("----------" + userDAO.get("yyyyyy"));
        Thread.sleep(3000);
        System.out.println(userDAO.ttl("yyyyyy"));
        System.out.println("----------" + userDAO.get("yyyyyy"));
        System.out.println(userDAO.keys("*"));
        String key = UUID.randomUUID().toString();
        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
        //System.out.println(userDAO.keys("*"));
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
        //System.out.println(userDAO.keys("*"));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
        int cnt = 0;
        cnt++;
        userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false));
        if (cnt % 10000 == 0) {
            System.out.println(cnt);
        }

    }
}