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
@Table(name = "tb_repair_operate_info")
@Comment("车辆运营报修单表")
public class TbRepairOperateInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("维修状态")
  @NumberColumn(name = "STATUS", nullable= false, type = NumberType.INTEGER)
  Integer status;

  @Comment("故障类型")
  @StringColumn(name = "FAULT_TYPE", nullable=false, type = StringType.VARCHAR)
  String faultType;

  @Comment("故障信息")
  @StringColumn(name = "FAULT_MESSAGE", nullable=false, type = StringType.VARCHAR)
  String faultMessage;

  @Comment("车牌号")
  @StringColumn(name = "VEHICLE_NUM", nullable=false, type = StringType.VARCHAR)
  String vehicleNum;

  @Comment("车辆类型")
  @StringColumn(name = "VEHICLE_TYPE", nullable=false, type = StringType.VARCHAR)
  String vehicleType;

  @Comment("投放点名称")
  @StringColumn(name = "SPOT_NAME", nullable=false, type = StringType.VARCHAR)
  String spotName;

  @Comment("投放点地址 ")
  @StringColumn(name = "SPOT_ADRESS", nullable=false, type = StringType.VARCHAR)
  String spotAdress;

  @Comment("商家电话")
  @StringColumn(name = "SPOT_PHONE", nullable=false, type = StringType.VARCHAR)
  String spotPhone;

  @Comment("申请人")
  @StringColumn(name = "APPLY_PERSON", nullable=false, type = StringType.VARCHAR)
  String applyPerson;

  @Comment("申请时间")
  @DateColumn(name = "APPLY_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp applyTime;

  @Comment("完成时间")
  @DateColumn(name = "FINISH_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp finishTime;

  @Comment("维修人")
  @StringColumn(name = "REPAIR_PERSION", nullable=true, type = StringType.VARCHAR)
  String repairPersion;

  @Comment("添加人ID")
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
