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
@Table(name = "tb_log_vehicle_operation_info")
@Comment("车辆操作日志表")
public class TbLogVehicleOperationInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("操作类型(1-车辆状态，2-车辆指令)")
  @NumberColumn(name = "TYPE", nullable= false, type = NumberType.INTEGER)
  Integer type;

  @Comment("操作指令（启动、信号）")
  @NumberColumn(name = "RECORD", nullable= false, type = NumberType.INTEGER)
  Integer record;

  @Comment("操作车辆ID")
  @StringColumn(name = "VEHICLE_ID", nullable=false, type = StringType.VARCHAR)
  String vehicleId;

  @Comment("操作人")
  @StringColumn(name = "USER_NAME", nullable=false, type = StringType.VARCHAR)
  String userName;

  @Comment("操作结果（0-成功，1-失败）")
  @NumberColumn(name = "RESULT", nullable= false, type = NumberType.INTEGER)
  Integer result;

  @Comment("操作时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

}
