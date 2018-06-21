package com.rnkrsoft.framework.mongo.proxy;

import com.rnkrsoft.framework.orm.Entity;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Data
@ToString
@MongoTable(name = "OPERATE_LOG", schema = "mongo_data")
public class OperateLogEntity implements Serializable{
    @PrimaryKey
    @MongoColumn(valueMode = ValueMode.LT)
    String operateId;

    @MongoColumn(name = "NAME", valueMode = ValueMode.EQUAL)
    String name;
}
