package com.rnkrsoft.framework.orm.jdbc;

/**
 * Created by rnkrsoft.com on 2018/8/6.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class BaseEntity extends OrderByEntity {
    @Comment("创建日期")
    @DateColumn(name = "CREATE_DATE", nullable = false, type = DateType.TIMESTAMP, currentTimestamp = true)
    Date createDate;

    @Comment("更新日期")
    @DateColumn(name = "LAST_UPDATE_DATE", nullable = false, type = DateType.TIMESTAMP, onUpdate = true, currentTimestamp = true)
    Date lastUpdateDate;
}
