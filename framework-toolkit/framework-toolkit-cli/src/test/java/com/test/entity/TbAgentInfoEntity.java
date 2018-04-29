package com.test.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

import com.rnkrsoft.framework.orm.Table;
import com.rnkrsoft.framework.orm.Comment;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.StringColumn;
import com.rnkrsoft.framework.orm.NumberColumn;
import com.rnkrsoft.framework.orm.DateColumn;
import com.rnkrsoft.framework.orm.StringType;
import com.rnkrsoft.framework.orm.NumberType;
import com.rnkrsoft.framework.orm.DateType;

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
@Table(name = "tb_agent_info")
@Comment("代理信息表")
public class TbAgentInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("代理名字")
  @StringColumn(name = "AGENT_NAME", nullable=false, type = StringType.VARCHAR)
  String agentName;

  @Comment("代理电话")
  @StringColumn(name = "PHONE", nullable=false, type = StringType.VARCHAR)
  String phone;

  @Comment("代理住址")
  @StringColumn(name = "ADDRESS", nullable=true, type = StringType.VARCHAR)
  String address;

  @Comment("省份")
  @StringColumn(name = "PROVINCE", nullable=true, type = StringType.VARCHAR)
  String province;

  @Comment("城市")
  @StringColumn(name = "CITY", nullable=true, type = StringType.VARCHAR)
  String city;

  @Comment("区/县")
  @StringColumn(name = "REGION", nullable=true, type = StringType.VARCHAR)
  String region;

  @Comment("创建人ID")
  @StringColumn(name = "ADD_USER_ID", nullable=false, type = StringType.VARCHAR)
  String addUserId;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("修改人ID")
  @StringColumn(name = "UPDATE_USER_ID", nullable=false, type = StringType.VARCHAR)
  String updateUserId;

  @Comment("修改时间")
  @DateColumn(name = "UPDATE_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp updateTime;

}