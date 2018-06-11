package com.test.entity;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

import com.rnkrsoft.framework.orm.jdbc.Table;
import com.rnkrsoft.framework.orm.jdbc.Comment;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.StringColumn;
import com.rnkrsoft.framework.orm.jdbc.NumberColumn;
import com.rnkrsoft.framework.orm.jdbc.DateColumn;
import com.rnkrsoft.framework.orm.jdbc.StringType;
import com.rnkrsoft.framework.orm.jdbc.NumberType;
import com.rnkrsoft.framework.orm.jdbc.DateType;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * created by rnkrsoft.com ORM framework auto generate!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "ORM_DEMO_INF")
@Comment("演示信息表")
public class OrmDemoInfEntity implements Serializable{

  @Comment("序列号")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "SERIAL_NO", nullable=false, type = StringType.VARCHAR)
  String serialNo;

  @Comment("姓名")
  @StringColumn(name = "USER_NAME", nullable=false, type = StringType.VARCHAR)
  String userName;

  @Comment("年龄")
  @NumberColumn(name = "AGE", nullable= false, type = NumberType.INTEGER)
  Integer age;

  @Comment("创建日期")
  @DateColumn(name = "CREATE_DATE", nullable= false, type = DateType.DATE)
  Date createDate;

  @Comment("更新日期")
  @DateColumn(name = "LAST_UPDATE_DATE", nullable= false, type = DateType.TIMESTAMP)
  Timestamp lastUpdateDate;

}