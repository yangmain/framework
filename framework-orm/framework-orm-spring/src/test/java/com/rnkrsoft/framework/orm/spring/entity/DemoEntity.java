package com.rnkrsoft.framework.orm.spring.entity;

import com.rnkrsoft.framework.orm.Comment;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.StringColumn;
import com.rnkrsoft.framework.orm.Table;

import java.io.Serializable;

/**
 * Created by woate on 2018/4/5.
 */
@Table(name = "DEMO_INF")
@Comment("演示信息表")
public class DemoEntity implements Serializable {
    @PrimaryKey
    @StringColumn(name = "SERIAL_NO")
    @Comment("序列号")
    String serialNo;
    @StringColumn(name = "AGE")
    @Comment("年龄")
    Integer age;
}
