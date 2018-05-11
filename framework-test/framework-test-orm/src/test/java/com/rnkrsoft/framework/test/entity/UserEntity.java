package com.rnkrsoft.framework.test.entity;

import com.rnkrsoft.framework.orm.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by woate on 2018/4/30.
 */
@Data
@Table(name = "USER_INF")
@Comment("用户信息表")
public class UserEntity {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID, feature = "${yyyyMMddHHmmssSSS}_${SEQ:9}_${RANDOM:5}")
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @Comment("序列号")
    String serialNo;

    @StringColumn(name = "USER_NAME", nullable = false)
    @Comment("姓名")
    String userName;

    @DateColumn(name = "CREATE_DATE", nullable = false)
    @Comment("创建日期")
    Date createDate;

    @DateColumn(name = "LAST_UPDATE_DATE", nullable = false)
    @Comment("更新日期")
    Timestamp lastUpdateDate;
}
