package com.rnkrsoft.framework.test.entity;

import com.rnkrsoft.framework.orm.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
@Data
@Table(name = "ORM_DEMO_INF")
@Comment("演示信息表")
public class OrmEntity extends Entity implements Serializable {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID, feature = "${yyyyMMddHHmmssSSS}_${SEQ:9}_${RANDOM:5}")
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @Comment("序列号")
    String serialNo;

    @StringColumn(name = "USER_NAME", nullable = false)
    @Comment("姓名")
    String userName;

    @NumberColumn(name = "AGE", nullable = false)
    @Comment("年龄")
    Integer age;

    @DateColumn(name = "CREATE_DATE", nullable = false)
    @Comment("创建日期")
    Date createDate;

    @DateColumn(name = "LAST_UPDATE_DATE", nullable = false)
    @Comment("更新日期")
    Timestamp lastUpdateDate;
}
