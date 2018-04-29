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
@Table(name = "tb_spot_commission_info")
@Comment("投放点分成比例表")
public class TbSpotCommissionInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("投放点ID")
  @StringColumn(name = "SPOT_ID", nullable=false, type = StringType.VARCHAR)
  String spotId;

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

}
