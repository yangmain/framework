package com.rnkrsoft.framework.orm.mongo.example.example3.entity;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/6.
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Inner1Object implements Serializable{
    @MongoColumn(name = "NAME", logicMode = LogicMode.OR, valueMode = ValueMode.EQUAL)
    String name;

    @MongoColumn(name = "AGE", logicMode = LogicMode.AND, valueMode = ValueMode.LT)
    Integer age;

    @MongoColumn(name = "AGE1", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    int age1;

    @MongoColumn(name = "time", logicMode = LogicMode.AND, valueMode = ValueMode.GT)
    Long time;

    @MongoColumn(name = "time1", logicMode = LogicMode.OR, valueMode = ValueMode.GTE)
    long time1;

    @MongoColumn(name = "money", logicMode = LogicMode.AND, valueMode = ValueMode.GTE)
    Double money;

    @MongoColumn(name = "money1", logicMode = LogicMode.OR, valueMode = ValueMode.EQUAL)
    double money1;

    @MongoColumn(name = "CREATE_DATE", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    Date createDate;

    @MongoColumn(name = "inner2Object", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    Inner2Object inner2Object;
}
