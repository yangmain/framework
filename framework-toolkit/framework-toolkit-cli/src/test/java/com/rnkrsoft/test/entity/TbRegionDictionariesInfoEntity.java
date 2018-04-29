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
@Table(name = "tb_region_dictionaries_info")
@Comment("区域字典")
public class TbRegionDictionariesInfoEntity implements Serializable{

  @Comment("")
  @PrimaryKey(strategy = PrimaryKeyStrategy.UUID )
  @StringColumn(name = "ID", nullable=false, type = StringType.VARCHAR)
  String id;

  @Comment("")
  @StringColumn(name = "FULL_NAME", nullable=false, type = StringType.VARCHAR)
  String fullName;

  @Comment("")
  @StringColumn(name = "NAME", nullable=false, type = StringType.VARCHAR)
  String name;

  @Comment("")
  @StringColumn(name = "TREE_PATH", nullable=true, type = StringType.VARCHAR)
  String treePath;

  @Comment("")
  @StringColumn(name = "PARENT_ID", nullable=true, type = StringType.VARCHAR)
  String parentId;

}
