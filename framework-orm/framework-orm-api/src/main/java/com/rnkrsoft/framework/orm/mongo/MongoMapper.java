package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.mongo.count.MongoCountMapper;
import com.rnkrsoft.framework.orm.mongo.delete.MongoDeleteMapper;
import com.rnkrsoft.framework.orm.mongo.insert.MongoInsertMapper;
import com.rnkrsoft.framework.orm.mongo.select.MongoSelectMapper;
import com.rnkrsoft.framework.orm.mongo.update.MongoUpdateMapper;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 * MongoDB数据库的DAO接口
 */
public interface MongoMapper<Entity> extends MongoInsertMapper<Entity>,
        MongoDeleteMapper<Entity>,
        MongoUpdateMapper<Entity>,
        MongoSelectMapper<Entity>,
        MongoCountMapper<Entity>
{
}
