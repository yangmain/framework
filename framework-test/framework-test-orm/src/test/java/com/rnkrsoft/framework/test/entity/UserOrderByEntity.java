package com.rnkrsoft.framework.test.entity;

import com.rnkrsoft.framework.orm.*;
import com.rnkrsoft.framework.orm.jdbc.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/4/30.
 */
@Data
@Table(name = "USER_INF", prefix = "TB", suffix = "INF")
@Comment("用户信息表")
public class UserOrderByEntity extends OrderByEntity {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID, feature = "${yyyyMMddHHmmssSSS}_${SEQ:9}_${RANDOM:5}")
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @Comment("序列号")
    String serialNo;

    @StringColumn(name = "USER_NAME", nullable = false)
    @Comment("姓名")
    String userName;

    @NumberColumn(name = "AGE1", nullable = false)
    @Comment("年龄")
    Long age1;

    @NumberColumn(name = "AGE2", nullable = false)
    @Comment("年龄2")
    Long age2;


    @DateColumn(name = "CREATE_DATE", nullable = false)
    @Comment("创建日期")
    Date createDate;

    @DateColumn(name = "LAST_UPDATE_DATE", nullable = false)
    @Comment("更新日期")
    Timestamp lastUpdateDate;
}
