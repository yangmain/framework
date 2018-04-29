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
@Table(name = "tb_vehicle_online_state_info")
@Comment("车辆在线情况表")
public class TbVehicleOnlineStateInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("车辆ID")
  @StringColumn(name = "VEHICLE_ID", nullable=false, type = StringType.VARCHAR)
  String vehicleId;

  @Comment("车牌号")
  @StringColumn(name = "VEHICLE_NUM", nullable=false, type = StringType.VARCHAR)
  String vehicleNum;

  @Comment("在线状态（0-离线，1-在线）")
  @NumberColumn(name = "STATUS", nullable= false, type = NumberType.INTEGER)
  Integer status;

  @Comment("离线天数")
  @NumberColumn(name = "OFFLINE_DAY", nullable= true, type = NumberType.INTEGER)
  Integer offlineDay;

}
