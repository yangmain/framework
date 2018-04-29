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
@Table(name = "tb_customer_wallet_bill_info")
@Comment("用户钱包入账记表")
public class TbCustomerWalletBillInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("用户id")
  @StringColumn(name = "CUSTOMER_ID", nullable=false, type = StringType.VARCHAR)
  String customerId;

  @Comment("消费方式，微信，余额，券")
  @NumberColumn(name = "PAYMENT_METHOD_ID", nullable= false, type = NumberType.INTEGER)
  Integer paymentMethodId;

  @Comment("消费，充值")
  @NumberColumn(name = "PAYMENT_TYPE", nullable= false, type = NumberType.INTEGER)
  Integer paymentType;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("状态（0失败，1成功）")
  @NumberColumn(name = "STATUS", nullable= false, type = NumberType.INTEGER)
  Integer status;

  @Comment("当前余额")
  @NumberColumn(name = "BUSE", nullable= false, type = NumberType.INTEGER)
  Integer buse;

  @Comment("订单号")
  @StringColumn(name = "SN_ID", nullable=false, type = StringType.VARCHAR)
  String snId;

  @Comment("记录说明")
  @StringColumn(name = "REMARK", nullable=false, type = StringType.VARCHAR)
  String remark;

  @Comment("操作人")
  @StringColumn(name = "OPERATION_ID", nullable=false, type = StringType.VARCHAR)
  String operationId;

  @Comment("流水号")
  @StringColumn(name = "TRADE_NO", nullable=false, type = StringType.VARCHAR)
  String tradeNo;

}
