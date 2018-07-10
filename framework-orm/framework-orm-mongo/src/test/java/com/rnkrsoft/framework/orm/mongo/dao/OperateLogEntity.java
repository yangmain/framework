package com.rnkrsoft.framework.orm.mongo.dao;

import com.rnkrsoft.framework.orm.Entity;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import lombok.*;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MongoTable(name = "OPERATE_LOG", schema = "mongo_data")
public class OperateLogEntity implements Serializable{
    @PrimaryKey(strategy = PrimaryKeyStrategy.EXPRESSION, feature = "ABCD_${yyyyMMddHHmmssSSS}_${SEQ:5}_${SEQ:8}${SEQ:8}_${RANDOM:8}_EFGH")
    @MongoColumn(valueMode = ValueMode.EQUAL, nullable = false)
    String id;

    @MongoColumn(name = "NAME", valueMode = ValueMode.EQUAL)
    String name;

    @MongoColumn(name = "AGE", valueMode = ValueMode.GT)
    Integer age;

    @MongoColumn(name = "DATA", valueMode = ValueMode.LIKE)
    String data;
}
