package com.rnkrsoft.framework.test.entity;

import com.rnkrsoft.framework.orm.*;
import com.rnkrsoft.framework.orm.jdbc.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
@Data
@Table(name = "ORDER", prefix = "TB", suffix = "INF")
@Comment("演示信息表")
public class OrderEntity extends Entity implements Serializable {
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

    @NumberColumn(name = "FLAG", nullable = false)
    @Comment("表示")
    Long flag;

    @NumberColumn(name = "STATUS", nullable = false)
    @Comment("状态")
    Boolean status;

    @DateColumn(name = "CREATE_DATE", nullable = false, type = DateType.TIMESTAMP)
    @Comment("创建日期")
    Date createDate;

    @DateColumn(name = "LAST_UPDATE_DATE", nullable = false)
    @Comment("更新日期")
    Timestamp lastUpdateDate;
}
