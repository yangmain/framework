package com.rnkrsoft.framework.orm.mongo.entity;

import com.rnkrsoft.framework.orm.*;
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
@MongoTable(name = "OPERATE_LOG", schema = "xxxx")
public class OperateLogEntity implements Serializable{
    @PrimaryKey(strategy = PrimaryKeyStrategy.EXPRESSION, feature = "ABCD_${yyyyMMddHHmmssSSS}_${SEQ:5}_${SEQ:8}${SEQ:8}_${RANDOM:8}_EFGH")
    @MongoColumn(name = "_id", valueMode = ValueMode.EQUAL, nullable = false)
    String id;

    @MongoColumn(name = "NAME", logicMode = LogicMode.OR, valueMode = ValueMode.EQUAL)
    String name;

    @MongoColumn(name = "AGE", logicMode = LogicMode.AND, valueMode = ValueMode.GT)
    Integer age;

    @MongoColumn(name = "DATA", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    String data;
}
