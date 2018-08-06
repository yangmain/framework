package com.rnkrsoft.framework.orm.mongo.example.example2.entity;

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
public class ObjectVO implements Serializable{
    @MongoColumn(name = "NAME", logicMode = LogicMode.OR, valueMode = ValueMode.EQUAL)
    String name;

    @MongoColumn(name = "AGE", logicMode = LogicMode.AND, valueMode = ValueMode.EQUAL)
    Integer age;

    @MongoColumn(name = "AGE1", logicMode = LogicMode.AND, valueMode = ValueMode.EQUAL)
    int age1;

    @MongoColumn(name = "time", logicMode = LogicMode.AND, valueMode = ValueMode.EQUAL)
    Long time;

    @MongoColumn(name = "time1", logicMode = LogicMode.AND, valueMode = ValueMode.EQUAL)
    long time1;

    @MongoColumn(name = "CREATE_DATE", logicMode = LogicMode.OR, valueMode = ValueMode.LTE)
    Date createDate;
}
