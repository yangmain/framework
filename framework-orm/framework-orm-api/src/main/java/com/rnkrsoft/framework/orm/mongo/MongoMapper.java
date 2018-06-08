package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.mongo.count.CountMongoMapper;
import com.rnkrsoft.framework.orm.mongo.delete.DeleteMongoMapper;
import com.rnkrsoft.framework.orm.mongo.insert.InsertMongoMapper;
import com.rnkrsoft.framework.orm.mongo.select.SelectMongoMapper;
import com.rnkrsoft.framework.orm.mongo.update.UpdateMongoMapper;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 * MongoDB数据库的DAO接口
 */
public interface MongoMapper<Entity> extends InsertMongoMapper<Entity>,
        DeleteMongoMapper<Entity>,
        UpdateMongoMapper<Entity>,
        SelectMongoMapper<Entity>,
        CountMongoMapper<Entity>
{
}
