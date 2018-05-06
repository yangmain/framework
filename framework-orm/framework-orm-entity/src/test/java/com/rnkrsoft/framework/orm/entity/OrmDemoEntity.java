package com.rnkrsoft.framework.orm.entity;

import com.rnkrsoft.framework.orm.*;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/4/9.
 */
@Table(name = "DEMO", prefix = "TB", suffix = "INF")
@Comment("xxxx")
public class OrmDemoEntity implements Serializable {
    @StringColumn(name = "SERIAL_NO", nullable = false)
    @PrimaryKey
    String serialNo;
    @NumberColumn(name = "AGE", nullable = false)
    Integer age;
    @DateColumn(name = "CREATE_DATE")
    Date createDate;
}
