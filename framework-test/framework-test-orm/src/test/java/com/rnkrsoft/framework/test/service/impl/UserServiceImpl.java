package com.rnkrsoft.framework.test.service.impl;

import com.rnkrsoft.framework.test.dao.OrderDAO;
import com.rnkrsoft.framework.test.entity.OrderEntity;
import com.rnkrsoft.framework.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by woate on 2018/4/26.
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    OrderDAO orderDAO;

    @Override
    public String register(String name, int age) {
        if(name == null){
            return null;
        }
        if(name.equals("!@#$%")){
            throw new IllegalArgumentException("无效用户名");
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setAge(age);
        orderEntity.setUserName(name);
        orderEntity.setCreateDate(new Date());
        orderEntity.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        orderDAO.insert(orderEntity);
        return orderEntity.getSerialNo();
    }
}
