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
@Table(name = "tb_coupon_info")
@Comment("用户券表")
public class TbCouponInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("用户ID")
  @StringColumn(name = "CUSTOMER_ID", nullable=false, type = StringType.VARCHAR)
  String customerId;

  @Comment("编码")
  @StringColumn(name = "CODE", nullable=false, type = StringType.VARCHAR)
  String code;

  @Comment("模板ID")
  @StringColumn(name = "COUPON_TEMPLATE_ID", nullable=false, type = StringType.VARCHAR)
  String couponTemplateId;

  @Comment("状态 （使用其情况：0-未使用，1-已使用，2-已失效）")
  @NumberColumn(name = "STATUS", nullable= false, type = NumberType.INTEGER)
  Integer status;

  @Comment("领取时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("到期时间")
  @DateColumn(name = "END_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp endTime;

  @Comment("使用时间")
  @DateColumn(name = "USE_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp useTime;

  @Comment("对应订单ID")
  @StringColumn(name = "ORDER_ID", nullable=true, type = StringType.VARCHAR)
  String orderId;

}
