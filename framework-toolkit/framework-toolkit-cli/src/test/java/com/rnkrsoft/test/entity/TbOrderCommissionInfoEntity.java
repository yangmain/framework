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
@Table(name = "tb_order_commission_info")
@Comment("订单与分成关联表")
public class TbOrderCommissionInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("订单id")
  @StringColumn(name = "ORDER_ID", nullable=false, type = StringType.VARCHAR)
  String orderId;

  @Comment("公司分成")
  @NumberColumn(name = "COMPANY_FC", nullable= false, type = NumberType.INTEGER)
  Integer companyFc;

  @Comment("代理分成")
  @NumberColumn(name = "ONE_AGENT_FC", nullable= false, type = NumberType.INTEGER)
  Integer oneAgentFc;

  @Comment("二级代理分成")
  @NumberColumn(name = "TWO_AGENT_FC", nullable= false, type = NumberType.INTEGER)
  Integer twoAgentFc;

  @Comment("三级代理分成")
  @NumberColumn(name = "THREE_AGEN_FC", nullable= false, type = NumberType.INTEGER)
  Integer threeAgenFc;

  @Comment("托管人分成")
  @NumberColumn(name = "CUSTODIAN_FC", nullable= false, type = NumberType.INTEGER)
  Integer custodianFc;

  @Comment("投放点分成")
  @NumberColumn(name = "SPOT_FC", nullable= false, type = NumberType.INTEGER)
  Integer spotFc;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

}
