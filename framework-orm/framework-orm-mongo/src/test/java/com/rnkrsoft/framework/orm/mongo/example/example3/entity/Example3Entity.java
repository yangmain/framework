package com.rnkrsoft.framework.orm.mongo.example.example3.entity;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MongoTable(name = "OPERATE_LOG", schema = "xxxx")
public class Example3Entity implements Serializable{
    @PrimaryKey(strategy = PrimaryKeyStrategy.EXPRESSION, feature = "ABCD_${yyyyMMddHHmmssSSS}_${SEQ:5}_${SEQ:8}${SEQ:8}_${RANDOM:8}_EFGH")
    @MongoColumn(name = "_id", valueMode = ValueMode.EQUAL, nullable = false)
    String id;

    @MongoColumn(name = "NAME", logicMode = LogicMode.OR, valueMode = ValueMode.EQUAL)
    String name;

    @MongoColumn(name = "AGE", logicMode = LogicMode.AND, valueMode = ValueMode.EQUAL)
    Integer age;

    @MongoColumn(name = "DATA", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    String data;

    @MongoColumn(name = "CREATE_DATE", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    Date createDate;

    @MongoColumn(name = "AMT", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    BigDecimal amt;

    @MongoColumn(name = "inner1Object", logicMode = LogicMode.OR, valueMode = ValueMode.NONE)
    Inner1Object inner1Object;

    @MongoColumn(name = "inner1Object2", logicMode = LogicMode.OR, valueMode = ValueMode.NONE)
    Inner1Object inner1Object2;
}
