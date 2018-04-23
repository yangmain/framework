package com.rnkrsoft.framework.toolkit.generator.entity;

import com.rnkrsoft.framework.orm.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
@Data
@Table(name = "ORM_DEMO_INF")
@Comment("演示信息表")
public class OrmEntity implements Serializable {
    @PrimaryKey(strategy = PrimaryKeyStrategy.EXPRESSION, feature = "${yyyyMMddHHmmssSSS}_${SEQ:9}")
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @Comment("序列号")
    String serialNo;
    @NumberColumn(name = "AGE", nullable = false)
    @Comment("年龄")
    Integer age;
}
