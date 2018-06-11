package com.rnkrsoft.framework.sequence.myisam.entity;

import com.rnkrsoft.framework.orm.jdbc.Comment;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngine;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/4/22.
 * 用于利用MyISAM引擎特性生成全局唯一连续有序序号
 */
@Table(name = "SEQUENCE_INF")
@DataEngine(DataEngineType.MyISAM)
@Comment("序号信息表")
public class SequenceEntity implements Serializable{
    @Id
    @Column(name = "SEQ_NAME", length = 36)
    @Comment("序列名称")
    String seqName;

    @Id
    @Column(name = "SEQ_FEATURE")
    @Comment("序列特征")
    String seqFeature;

    @Id
    @Column(name = "SEQ_VALUE", nullable = false)
    @GeneratedValue
    @Comment("序列值")
    int seqValue;
}
