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
@Table(name = "tb_custodian_info")
@Comment("托管人信息表")
public class TbCustodianInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("托管人姓名")
  @StringColumn(name = "CUSTODIAN_NAME", nullable=false, type = StringType.VARCHAR)
  String custodianName;

  @Comment("托管人电话")
  @StringColumn(name = "PHONE", nullable=false, type = StringType.VARCHAR)
  String phone;

  @Comment("托管人地址")
  @StringColumn(name = "ADDRESS", nullable=false, type = StringType.VARCHAR)
  String address;

  @Comment("公司分成")
  @NumberColumn(name = "COMPANY_FC", nullable= false, type = NumberType.INTEGER)
  Integer companyFc;

  @Comment("托管人分成")
  @NumberColumn(name = "CUSTODIAN_FC", nullable= false, type = NumberType.INTEGER)
  Integer custodianFc;

  @Comment("投放点分成")
  @NumberColumn(name = "SPOT_FC", nullable= false, type = NumberType.INTEGER)
  Integer spotFc;

  @Comment("创建人ID")
  @StringColumn(name = "ADD_USER_ID", nullable=false, type = StringType.VARCHAR)
  String addUserId;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("修改人ID")
  @StringColumn(name = "UPDATE_USER_ID", nullable=false, type = StringType.VARCHAR)
  String updateUserId;

  @Comment("修改时间")
  @DateColumn(name = "UPDATE_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp updateTime;

}
