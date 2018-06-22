package com.rnkrsoft.framework.mongo.proxy;

import com.mongodb.MongoClient;
import com.rnkrsoft.framework.orm.ValueByColumn;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.mongo.proxy.MongoProxyFactory;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public class OperateLogDAOTest {

    @Test
    public void test1(){
        OperateLogDAO operateLogDAO = new MongoProxyFactory<OperateLogDAO>(OperateLogDAO.class,new MongoClient("localhost", 3789)).newInstance();
        OperateLogEntity operateLogEntity = new OperateLogEntity();
        ValueByColumn valueByColumn = new ValueByColumn();
        valueByColumn.setColumn("test1");
        valueByColumn.setValueMode(ValueMode.EQUAL);
        operateLogDAO.selectRuntime(operateLogEntity);
    }
}