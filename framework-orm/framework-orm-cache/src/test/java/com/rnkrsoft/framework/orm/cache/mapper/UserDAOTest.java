package com.rnkrsoft.framework.orm.cache.mapper;

import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by woate on 2018/6/8.
 */
@ContextConfiguration(locations = "classpath*:testContext-cache.xml")
public class UserDAOTest extends SpringTest {
    @Autowired
    UserDAO userDAO;

    @Test
    public void testSet() throws Exception {

//        userDAO.expire("xxxxxxx", 60);
//        Thread.sleep(50 * 1000);
        while (true) {
//            System.out.println( userDAO.incr("xxxxxxx"));
//            System.out.println(userDAO.decr("xxxxxxx"));
//            System.out.println(userDAO.get1("xxxxxxx"));
            System.out.println(userDAO.getSet("yyyyyy", new User(UUID.randomUUID().toString(), 21, false)));
//            System.out.println(userDAO.get("yyyyyy"));
            userDAO.expire("yyyyyy", 2);
            System.out.println(userDAO.ttl("yyyyyy"));
//            userDAO.presist("yyyyyy");
            Thread.sleep(3000);
            System.out.println(userDAO.ttl("yyyyyy"));
            System.out.println("----------" + userDAO.get("yyyyyy"));
        }
        //System.out.println(userDAO.keys("*"));
//        String key = UUID.randomUUID().toString();
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        //System.out.println(userDAO.keys("*"));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(key, new User(UUID.randomUUID().toString(), 21, false)));
//        //System.out.println(userDAO.keys("*"));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        System.out.println(userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false)));
//        int cnt =0;
//        while (true){
//            cnt++;
//            userDAO.getSet(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), 21, false));
//            if (cnt%10000 ==0){
//                System.out.println(cnt);
//            }
//        }

    }
}