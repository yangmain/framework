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
@Table(name = "tb_customer_info")
@Comment("用户信息表")
public class TbCustomerInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("OPENID")
  @StringColumn(name = "OPEN_ID", nullable=false, type = StringType.VARCHAR)
  String openId;

  @Comment("用户昵称")
  @StringColumn(name = "NICKNAME", nullable=true, type = StringType.VARCHAR)
  String nickname;

  @Comment("头像")
  @StringColumn(name = "FEAD_PORTRAIT", nullable=true, type = StringType.VARCHAR)
  String feadPortrait;

  @Comment("手机号")
  @StringColumn(name = "PHONE", nullable=true, type = StringType.VARCHAR)
  String phone;

  @Comment("性别")
  @StringColumn(name = "GENDER", nullable=true, type = StringType.VARCHAR)
  String gender;

  @Comment("地区")
  @StringColumn(name = "REGION", nullable=true, type = StringType.VARCHAR)
  String region;

  @Comment("首次使用时间")
  @DateColumn(name = "FIRST_USE_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp firstUseTime;

  @Comment("注册时间")
  @DateColumn(name = "REGISTRATION_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp registrationTime;

  @Comment("最后登陆时间")
  @DateColumn(name = "LAST_LANDING_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp lastLandingTime;

}
