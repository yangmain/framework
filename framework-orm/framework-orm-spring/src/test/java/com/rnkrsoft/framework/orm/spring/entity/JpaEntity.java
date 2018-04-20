package com.rnkrsoft.framework.orm.spring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
@Data
@Table(name = "JPA_DEMO_INF")
public class JpaEntity implements Serializable {
    @Id
    @Column(name = "SERIAL_NO", nullable = false)
    String serialNo;
    @Column(name = "AGE", nullable = false)
    Integer age;
}
