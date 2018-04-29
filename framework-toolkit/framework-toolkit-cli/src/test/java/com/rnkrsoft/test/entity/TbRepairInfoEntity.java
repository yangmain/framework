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
@Table(name = "tb_repair_info")
@Comment("用户前端保修表")
public class TbRepairInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("车票号")
  @StringColumn(name = "VEHICLE_NUM", nullable=false, type = StringType.VARCHAR)
  String vehicleNum;

  @Comment("投放点名称")
  @StringColumn(name = "SPOT_NAME", nullable=false, type = StringType.VARCHAR)
  String spotName;

  @Comment("用户ID")
  @StringColumn(name = "CUSTOMER_ID", nullable=false, type = StringType.VARCHAR)
  String customerId;

  @Comment("用户名称")
  @StringColumn(name = "CUSTOMER_NAME", nullable=false, type = StringType.VARCHAR)
  String customerName;

  @Comment("故障类型")
  @StringColumn(name = "FAULT_TYPE", nullable=true, type = StringType.VARCHAR)
  String faultType;

  @Comment("故障图片")
  @StringColumn(name = "IMAGE", nullable=true, type = StringType.VARCHAR)
  String image;

  @Comment("故障描述")
  @StringColumn(name = "FAULT_MESSAGE", nullable=true, type = StringType.VARCHAR)
  String faultMessage;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

}
