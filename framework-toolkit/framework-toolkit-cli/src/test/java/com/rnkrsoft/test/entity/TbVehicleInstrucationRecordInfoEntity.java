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
@Table(name = "tb_vehicle_instrucation_record_info")
@Comment("指令记录表")
public class TbVehicleInstrucationRecordInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("设备号")
  @StringColumn(name = "DEVICE_ID", nullable=false, type = StringType.VARCHAR)
  String deviceId;

  @Comment("订单号")
  @StringColumn(name = "SN_ID", nullable=false, type = StringType.VARCHAR)
  String snId;

  @Comment("发送次数")
  @DateColumn(name = "SEND_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp sendTime;

  @Comment("接收时间")
  @DateColumn(name = "RECEVIE_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp recevieTime;

  @Comment("发送次数")
  @NumberColumn(name = "SEND_NUM", nullable= false, type = NumberType.INTEGER)
  Integer sendNum;

  @Comment("序列号")
  @StringColumn(name = "SERIAL_NUM", nullable=false, type = StringType.VARCHAR)
  String serialNum;

  @Comment("0-无返回，1-正常，2-延迟")
  @NumberColumn(name = "JUDGE_TYPE", nullable= false, type = NumberType.INTEGER)
  Integer judgeType;

}
