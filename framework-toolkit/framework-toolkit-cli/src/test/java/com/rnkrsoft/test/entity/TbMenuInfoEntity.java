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
@Table(name = "tb_menu_info")
@Comment("平台菜单配置")
public class TbMenuInfoEntity implements Serializable{

  @Comment("ID")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("所有父级ID [1],[2],[3]")
  @StringColumn(name = "ID_PATH", nullable=true, type = StringType.VARCHAR)
  String idPath;

  @Comment("根节点菜单唯一码")
  @StringColumn(name = "ROOT_ID", nullable=false, type = StringType.VARCHAR)
  String rootId;

  @Comment("父级菜单唯一码")
  @StringColumn(name = "PARENT_ID", nullable=false, type = StringType.VARCHAR)
  String parentId;

  @Comment("菜单名称")
  @StringColumn(name = "NAME", nullable=false, type = StringType.VARCHAR)
  String name;

  @Comment("图标")
  @StringColumn(name = "ICON", nullable=true, type = StringType.VARCHAR)
  String icon;

  @Comment("排序")
  @NumberColumn(name = "SORT_ID", nullable= false, type = NumberType.INTEGER)
  Integer sortId;

  @Comment("级别")
  @NumberColumn(name = "LEVEL", nullable= false, type = NumberType.INTEGER)
  Integer level;

  @Comment("操作类型0 菜单 1 操作 ")
  @NumberColumn(name = "OP_TYPE", nullable= false, type = NumberType.INTEGER)
  Integer opType;

  @Comment("是否左侧菜单,0否，1是")
  @NumberColumn(name = "IS_LEFT", nullable= false, type = NumberType.INTEGER)
  Integer isLeft;

  @Comment("是否导航菜单，0否，1是")
  @NumberColumn(name = "IS_NAV", nullable= false, type = NumberType.INTEGER)
  Integer isNav;

  @Comment("菜单地址")
  @StringColumn(name = "PAGE_URL", nullable=false, type = StringType.VARCHAR)
  String pageUrl;

  @Comment("权限码")
  @StringColumn(name = "PERMISSION_CODE", nullable=false, type = StringType.VARCHAR)
  String permissionCode;

  @Comment("是否删除 0 否 1是")
  @NumberColumn(name = "IS_DEL", nullable= false, type = NumberType.INTEGER)
  Integer isDel;

  @Comment("域名")
  @StringColumn(name = "DOMAIN", nullable=true, type = StringType.VARCHAR)
  String domain;

  @Comment("添加人ID")
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
