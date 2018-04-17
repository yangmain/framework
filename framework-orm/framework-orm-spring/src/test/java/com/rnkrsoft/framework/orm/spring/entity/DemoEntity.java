package com.rnkrsoft.framework.orm.spring.entity;

import com.rnkrsoft.framework.orm.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
@Data
@Table(name = "DEMO_INF")
@Comment("演示信息表")
public class DemoEntity implements Serializable {
    @PrimaryKey(strategy = PrimaryKeyStrategy.SEQUENCE_SERVICE, feature = PrimaryKeyFeatureConstant.YYYY_MM_DD_HH_MM_SS_SSS)
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @Comment("序列号")
    String serialNo;
    @NumberColumn(name = "AGE", nullable = false)
    @Comment("年龄")
    Integer age;
}
