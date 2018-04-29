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
@Table(name = "tb_order_info")
@Comment("订单表")
public class TbOrderInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("订单号")
  @StringColumn(name = "SN_ID", nullable=false, type = StringType.VARCHAR)
  String snId;

  @Comment("订单状态")
  @NumberColumn(name = "STATUS", nullable= false, type = NumberType.INTEGER)
  Integer status;

  @Comment("支付方式 id")
  @StringColumn(name = "PAYMENT_METHOD_ID", nullable=false, type = StringType.VARCHAR)
  String paymentMethodId;

  @Comment("支付方式名称")
  @StringColumn(name = "PAYMENT_NAME", nullable=true, type = StringType.VARCHAR)
  String paymentName;

  @Comment("应付金额")
  @NumberColumn(name = "TOTAL_AMOUNT", nullable= false, type = NumberType.INTEGER)
  Integer totalAmount;

  @Comment("实付金额")
  @NumberColumn(name = "ACTUAL_AMOUNT", nullable= false, type = NumberType.INTEGER)
  Integer actualAmount;

  @Comment("流水号")
  @StringColumn(name = "TRADE_NO", nullable=true, type = StringType.VARCHAR)
  String tradeNo;

  @Comment("用户id")
  @StringColumn(name = "CUSTOMER_ID", nullable=false, type = StringType.VARCHAR)
  String customerId;

  @Comment("车辆id")
  @StringColumn(name = "VEHICLE_ID", nullable=false, type = StringType.VARCHAR)
  String vehicleId;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("支付时间")
  @DateColumn(name = "PAY_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp payTime;

  @Comment("投放点")
  @StringColumn(name = "SPOT_NAME", nullable=false, type = StringType.VARCHAR)
  String spotName;

  @Comment("分成信息id")
  @StringColumn(name = "DIVIDE_ID", nullable=false, type = StringType.VARCHAR)
  String divideId;

}
