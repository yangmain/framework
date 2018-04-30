package com.rnkrsoft.framework.test.service.impl;

import com.rnkrsoft.framework.test.dao.OrmDemoDAO;
import com.rnkrsoft.framework.test.entity.OrmEntity;
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
    OrmDemoDAO ormDemoDAO;

    @Override
    public String register(String name, int age) {
        if(name == null){
            return null;
        }
        if(name.equals("!@#$%")){
            throw new IllegalArgumentException("无效用户名");
        }
        OrmEntity ormEntity = new OrmEntity();
        ormEntity.setAge(age);
        ormEntity.setUserName(name);
        ormEntity.setCreateDate(new Date());
        ormEntity.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        ormDemoDAO.insert(ormEntity);
        return ormEntity.getSerialNo();
    }
}
