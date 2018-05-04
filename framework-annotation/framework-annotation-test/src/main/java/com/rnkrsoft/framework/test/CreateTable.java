package com.rnkrsoft.framework.test;


import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 用于测试类或者方法上标注的测试表相关信息
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateTable {
    /**
     * 所需要的实体类列表
     *
     * @return 实体类列表
     * @since 1.0.0
     */
    Class[] entities();

    /**
     * 数据库模式
     * @return 数据库模式
     * @since 1.0.0
     */
    String schema() default "";

    /**
     * 前缀
     * @return 前缀
     * @since 1.0.0
     */
    String prefix() default "";

    /**
     * 后缀
     * @return 后缀
     * @since 1.0.0
     */
    String suffix() default "";

    /**
     * 测试前创建表结构
     *
     * @return 真假值
     * @since 1.0.0
     */
    boolean createBeforeTest() default true;

    /**
     * 创建表结构前进行表结构的删除
     *
     * @return 真假值
     * @since 1.0.0
     */
    boolean dropBeforeCreate() default true;

    /**
     * 测试后删除表结构
     * @return 真假值
     * @since 1.0.0
     */
    boolean dropAfterTest() default true;
    /**
     * 关键字的单词模式
     * 例如select drop delete update where 等
     *
     * @return 单词模式
     * @since 1.0.0
     */
    WordMode keywordMode() default WordMode.lowerCase;

    /**
     * SQL语句使用的单词模式
     * 例如 select col1 FROM table1, col1和table1就是SQL语句
     * 如果为upperCase,则转换为SELECT col1 FROM table1
     * 如果为lowerCase,则转换为select col1 from table1
     *
     * @return 单词模式
     * @since 1.0.0
     */
    WordMode sqlMode() default WordMode.lowerCase;

    /**
     * 表模式使用
     * @return
     * @since 1.0.0
     */
    NameMode schemaMode() default NameMode.customize;

    /**
     * 前缀模式
     * @return
     * @since 1.0.0
     */
    NameMode prefixMode() default NameMode.customize;

    /**
     * 后缀模式
     * @return
     * @since 1.0.0
     */
    NameMode suffixMode() default NameMode.customize;
}
