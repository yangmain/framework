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
@Table(name = "tb_salesman_info")
@Comment("业务员信息表")
public class TbSalesmanInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("姓名")
  @StringColumn(name = "NAME", nullable=false, type = StringType.VARCHAR)
  String name;

  @Comment("性别")
  @NumberColumn(name = "GENDER", nullable= true, type = NumberType.INTEGER)
  Integer gender;

  @Comment("电话")
  @StringColumn(name = "PHONE", nullable=true, type = StringType.VARCHAR)
  String phone;

  @Comment("地址")
  @StringColumn(name = "ADRESS", nullable=true, type = StringType.VARCHAR)
  String adress;

  @Comment("添加时间")
  @DateColumn(name = "ADD_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp addTime;

  @Comment("添加人ID")
  @StringColumn(name = "ADD_USER_ID", nullable=false, type = StringType.VARCHAR)
  String addUserId;

  @Comment("修改人ID")
  @StringColumn(name = "UPDATE_USER_ID", nullable=false, type = StringType.VARCHAR)
  String updateUserId;

  @Comment("修改时间")
  @DateColumn(name = "UPDATE_TIME", nullable= false, type = DateType.TIMESTAMP)
  Timestamp updateTime;

}
