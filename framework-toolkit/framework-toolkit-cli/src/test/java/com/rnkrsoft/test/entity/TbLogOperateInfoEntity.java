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
@Table(name = "tb_log_operate_info")
@Comment("操作日志表")
public class TbLogOperateInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("IP地址")
  @StringColumn(name = "IP", nullable=true, type = StringType.VARCHAR)
  String ip;

  @Comment("操作用户ID")
  @StringColumn(name = "USER_ID", nullable=true, type = StringType.VARCHAR)
  String userId;

  @Comment("操作时间")
  @DateColumn(name = "OPERATE_TIME", nullable= true, type = DateType.TIMESTAMP)
  Timestamp operateTime;

  @Comment("关联表")
  @StringColumn(name = "LINK_TABLE", nullable=true, type = StringType.VARCHAR)
  String linkTable;

  @Comment("关联表ID")
  @StringColumn(name = "LINK_TABLE_ID", nullable=true, type = StringType.VARCHAR)
  String linkTableId;

  @Comment("操作类型")
  @StringColumn(name = "TYPE", nullable=true, type = StringType.VARCHAR)
  String type;

  @Comment("操作信息")
  @StringColumn(name = "OPERATE_MESSAGE", nullable=true, type = StringType.VARCHAR)
  String operateMessage;

  @Comment("备注")
  @StringColumn(name = "REMARK", nullable=true, type = StringType.VARCHAR)
  String remark;

}
