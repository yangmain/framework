package com.rnkrsoft.framework.test;


import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateTable {
    /**
     * 所需要的实体类列表
     *
     * @return 实体类列表
     */
    Class[] entities();

    /**
     * 数据库模式
     * @return 数据库模式
     */
    String schema() default "";

    /**
     * 前缀
     * @return
     */
    String prefix() default "";

    /**
     * 后缀
     * @return
     */
    String suffix() default "";

    /**
     * 创建表前进行测试
     *
     * @return 真假值
     */
    boolean createBeforeTest() default true;

    /**
     * 进行测试前进行数据清除
     *
     * @return 真假值
     */
    boolean testBeforeDrop() default true;

    /**
     * 关键字的单词模式
     * 例如select drop delete update where 等
     *
     * @return 单词模式
     */
    WordMode keywordMode() default WordMode.lowerCase;

    /**
     * SQL语句使用的单词模式
     * 例如 select col1 from table1, col1和table1就是SQL语句
     *
     * @return 单词模式
     */
    WordMode sqlMode() default WordMode.lowerCase;

    /**
     * 表模式使用
     * @return
     */
    NameMode schemaMode() default NameMode.customize;

    /**
     * 前缀模式
     * @return
     */
    NameMode prefixMode() default NameMode.customize;

    /**
     * 后缀模式
     * @return
     */
    NameMode suffixMode() default NameMode.customize;
}
