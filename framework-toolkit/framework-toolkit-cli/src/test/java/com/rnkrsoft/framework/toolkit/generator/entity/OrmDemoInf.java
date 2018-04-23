package com.rnkrsoft.framework.toolkit.generator.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

import com.rnkrsoft.framework.orm.Table;
import com.rnkrsoft.framework.orm.Comment;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.StringColumn;
import com.rnkrsoft.framework.orm.NumberColumn;
import com.rnkrsoft.framework.orm.DateColumn;

/**
 * created by rnkrsoft.com ORM framework auto generate!
 */
public class OrmDemoInf implements Serializable{

  @Comment("序列号")
  @PrimaryKey()
  @StringColumn(name = "SERIAL_NO")
  String serialNo;

  @Comment("年龄")
  @NumberColumn(name = "AGE")
  Integer age;

}