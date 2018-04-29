package com.rnkrsoft.test.entity;

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
 * 版权归氡氪网络科技有限公司所有 rnkrsoft.com 框架自动生成!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_spot_info")
@Comment("终端投放点信息表")
public class TbSpotInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("投放点名称")
  @StringColumn(name = "NAME", nullable=false, type = StringType.VARCHAR)
  String name;

  @Comment("行业")
  @StringColumn(name = "INDUSTRY", nullable=false, type = StringType.VARCHAR)
  String industry;

  @Comment("直属代理ID")
  @StringColumn(name = "AGENT_ID", nullable=false, type = StringType.VARCHAR)
  String agentId;

  @Comment("联系人ID")
  @StringColumn(name = "LINKMAN_ID", nullable=false, type = StringType.VARCHAR)
  String linkmanId;

  @Comment("业务员ID")
  @StringColumn(name = "SALESMAN_ID", nullable=false, type = StringType.VARCHAR)
  String salesmanId;

  @Comment("合同编号")
  @StringColumn(name = "CONTRACT_NUMBER", nullable=false, type = StringType.VARCHAR)
  String contractNumber;

  @Comment("托管人ID")
  @StringColumn(name = "CUSTODIAN_ID", nullable=false, type = StringType.VARCHAR)
  String custodianId;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("添加人ID")
  @StringColumn(name = "ADD_USER_ID", nullable=false, type = StringType.VARCHAR)
  String addUserId;

  @Comment("修改人ID")
  @StringColumn(name = "UPDATE_USER_ID", nullable=false, type = StringType.VARCHAR)
  String updateUserId;

  @Comment("修改时间")
  @DateColumn(name = "UPDATE_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp updateTime;

}
