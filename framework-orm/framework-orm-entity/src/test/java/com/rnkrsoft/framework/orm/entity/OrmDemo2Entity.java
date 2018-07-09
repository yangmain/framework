package com.rnkrsoft.framework.orm.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/4/9.
 */
@Table(name = "DEMO", prefix = "TB", suffix = "INF")
@Comment("xxxx")
public class OrmDemo2Entity implements Serializable {
    @NumberColumn(name = "SERIAL_NO", nullable = false)
    @PrimaryKey(strategy = PrimaryKeyStrategy.IDENTITY)
    Integer serialNo;
    @NumberColumn(name = "AGE", nullable = false)
    Integer age;
    @NumberColumn(name = "FLAG", nullable = false, type = NumberType.BYTE)
    Integer flag;
    @NumberColumn(name = "UID", nullable = false)
    Long uid;
    @DateColumn(name = "CREATE_DATE", currentTimestamp = true, nullable = false, type = DateType.TIMESTAMP)
    Date createDate;
    @DateColumn(name = "UPDATE_DATE", onUpdate = true, currentTimestamp = false, nullable = false, type = DateType.AUTO)
    Timestamp updateDate;
}
