package com.rnkrsoft.framework.monitor.heartbeat.entity;

import com.rnkrsoft.framework.orm.Comment;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.StringColumn;
import com.rnkrsoft.framework.orm.Table;
import com.rnkrsoft.framework.orm.mysql.DataEngine;
import com.rnkrsoft.framework.orm.mysql.DataEngineType;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
@Table(name = "HEARTBEAT", prefix = "TB", suffix = "INFO")
@DataEngine(DataEngineType.MyISAM)
@Comment("心跳信息表")
public class HeartbeatEntity {
    @PrimaryKey
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @Comment("物理主键")
    String serailNo;
}
