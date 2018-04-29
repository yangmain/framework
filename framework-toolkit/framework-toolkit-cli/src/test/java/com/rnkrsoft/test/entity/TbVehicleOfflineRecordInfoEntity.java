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
@Table(name = "tb_vehicle_offline_record_info")
@Comment("车辆离线情况表")
public class TbVehicleOfflineRecordInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("代理ID")
  @StringColumn(name = "AGENT_ID", nullable=false, type = StringType.VARCHAR)
  String agentId;

  @Comment("离线车辆数")
  @StringColumn(name = "OFFILE_NUM", nullable=false, type = StringType.VARCHAR)
  String offileNum;

  @Comment("离线车牌号")
  @StringColumn(name = "OFFILE_VEHICLE_NUM", nullable=false, type = StringType.VARCHAR)
  String offileVehicleNum;

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
