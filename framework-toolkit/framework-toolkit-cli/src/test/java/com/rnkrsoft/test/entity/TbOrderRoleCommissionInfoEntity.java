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
@Table(name = "tb_order_role_commission_info")
@Comment("订单角色分成映射表")
public class TbOrderRoleCommissionInfoEntity implements Serializable{

  @Comment("主键ID")
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("本级ID")
  @StringColumn(name = "ITSELF_ID", nullable=false, type = StringType.VARCHAR)
  String itselfId;

  @Comment("上级ID")
  @StringColumn(name = "HIGHER_LEVEL", nullable=false, type = StringType.VARCHAR)
  String higherLevel;

  @Comment("本级等级")
  @StringColumn(name = "CURRENT_LEVEL", nullable=false, type = StringType.VARCHAR)
  String currentLevel;

  @Comment("实有的分成")
  @NumberColumn(name = "TOTAL_FC", nullable= false, type = NumberType.INTEGER)
  Integer totalFc;

  @Comment("创建时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

}
