package com.rnkrsoft.framework.sequence.myisam.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.Comment;
import com.rnkrsoft.framework.orm.jdbc.StringColumn;
import com.rnkrsoft.framework.orm.jdbc.Table;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngine;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/4/22.
 * 用于利用MyISAM引擎特性生成全局唯一连续有序序号
 */
@Table(name = "SEQUENCE_INF", prefix = "TB")
@DataEngine(DataEngineType.MyISAM)
@Comment("序号信息表")
public class SequenceEntity implements Serializable{
    @PrimaryKey
    @StringColumn(name = "SEQ_NAME", length = 36)
    @Comment("序列名称")
    String seqName;

    @PrimaryKey
    @StringColumn(name = "SEQ_FEATURE")
    @Comment("序列特征")
    String seqFeature;

    @StringColumn(name = "SEQ_VALUE", nullable = false)
    @PrimaryKey(strategy = PrimaryKeyStrategy.IDENTITY)
    @Comment("序列值")
    int seqValue;
}
