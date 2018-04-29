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
@Table(name = "tb_vehicle_info")
@Comment("车辆信息表")
public class TbVehicleInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("车型ID")
  @StringColumn(name = "VEHICLE_TYPE_ID", nullable=true, type = StringType.VARCHAR)
  String vehicleTypeId;

  @Comment("车牌号")
  @StringColumn(name = "VEHICLE_NUMBER", nullable=true, type = StringType.VARCHAR)
  String vehicleNumber;

  @Comment("车辆图片")
  @StringColumn(name = "VEHICLE_PICTURE", nullable=true, type = StringType.VARCHAR)
  String vehiclePicture;

  @Comment("终端ID")
  @StringColumn(name = "TERMINAL_ID", nullable=true, type = StringType.VARCHAR)
  String terminalId;

  @Comment("终端密码")
  @StringColumn(name = "TERMINAL_PASSWORD", nullable=true, type = StringType.VARCHAR)
  String terminalPassword;

  @Comment("车辆保险ID")
  @StringColumn(name = "VEHICLE_INSURANCE_ID", nullable=true, type = StringType.VARCHAR)
  String vehicleInsuranceId;

  @Comment("车辆版本")
  @StringColumn(name = "VEHICLE_VERSION_ID", nullable=true, type = StringType.VARCHAR)
  String vehicleVersionId;

  @Comment("车辆定价套餐ID")
  @StringColumn(name = "VEHICLE_PRICE_ID", nullable=true, type = StringType.VARCHAR)
  String vehiclePriceId;

  @Comment("投放点ID")
  @StringColumn(name = "SPOT_ID", nullable=true, type = StringType.VARCHAR)
  String spotId;

  @Comment("运营模式（自运营AND代运营）")
  @StringColumn(name = "OPERATION_MODE", nullable=true, type = StringType.VARCHAR)
  String operationMode;

  @Comment("创建人ID")
  @StringColumn(name = "ADD_USER_ID", nullable=true, type = StringType.VARCHAR)
  String addUserId;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("修改人ID")
  @StringColumn(name = "UPDATE_USER_ID", nullable=true, type = StringType.VARCHAR)
  String updateUserId;

  @Comment("修改时间")
  @DateColumn(name = "UPDATE_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp updateTime;

}
