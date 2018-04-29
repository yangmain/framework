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
@Table(name = "tb_user_info")
@Comment("管理台用户信息表")
public class TbUserInfoEntity implements Serializable{

  @Comment("主键ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("角色ID")
  @StringColumn(name = "ROLE_ID", nullable=true, type = StringType.VARCHAR)
  String roleId;

  @Comment("登录名")
  @StringColumn(name = "USER_NAME", nullable=false, type = StringType.VARCHAR)
  String userName;

  @Comment("登录密码")
  @StringColumn(name = "PASSWORD", nullable=false, type = StringType.VARCHAR)
  String password;

  @Comment("真实姓名")
  @StringColumn(name = "REAL_NAME", nullable=true, type = StringType.VARCHAR)
  String realName;

  @Comment("性别")
  @StringColumn(name = "GENDER", nullable=false, type = StringType.VARCHAR)
  String gender;

  @Comment("电话")
  @StringColumn(name = "PHONE", nullable=true, type = StringType.VARCHAR)
  String phone;

  @Comment("邮箱")
  @StringColumn(name = "EMAIL", nullable=true, type = StringType.VARCHAR)
  String email;

  @Comment("状态（0：可用；1：禁用）")
  @StringColumn(name = "STATE", nullable=true, type = StringType.VARCHAR)
  String state;

  @Comment("禁用原因")
  @StringColumn(name = "STATE_DESCRIPTION", nullable=true, type = StringType.VARCHAR)
  String stateDescription;

  @Comment("最后登陆时间")
  @DateColumn(name = "LAST_ACCESS_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp lastAccessTime;

  @Comment("访问IP")
  @StringColumn(name = "ACCESS_IP", nullable=true, type = StringType.VARCHAR)
  String accessIp;

  @Comment("创建人名称")
  @StringColumn(name = "FOUNDER_NAME", nullable=false, type = StringType.VARCHAR)
  String founderName;

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
