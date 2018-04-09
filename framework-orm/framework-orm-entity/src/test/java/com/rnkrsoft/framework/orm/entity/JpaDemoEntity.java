package com.rnkrsoft.framework.orm.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/4/9.
 */
@Table(name = "JPA_DEMO_INFO")
public class JpaDemoEntity implements Serializable{
    @Column(name = "SERIAL_NO")
    @Id
    String serialNo;
    @Column(name = "AGE", nullable = false)
    Integer age;
    @Column(name = "CREATE_DATE")
    Date createDate;
}
