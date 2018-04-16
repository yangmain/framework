package com.rnkrsoft.framework.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 标注该注解的字段是主键，支持多个主键，但是数据访问对象只支持一个物理主键
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    /**
     * 主键生成规则
     *
     * @return 主键生成规则
     */
    PrimaryKeyStrategy strategy() default PrimaryKeyStrategy.UUID;

    /**
     * 用于指定主键生成的特征
     * <ol>
     * <li>${yyyyMMdd}</li>
     * <li>${yyyyMMddHH}</li>
     * <li>${yyyyMMddHHmm}</li>
     * <li>${yyyyMMddHHmmss}</li>
     * <li>${yyyyMMddHHmmssSSS}</li>
     * <li>固定字符串
     * <p>
     * </ol>
     * @see PrimaryKeyFeatureConstant
     * @return 主键特征
     */
    String feature() default "";

    /**
     * 序号是否作为前缀
     * @return 默认作为后缀
     */
    boolean seqNoPrefix() default false;
    /**
     * 填充长度
     * @return 长度
     */
    int fillLength() default 5;
}
