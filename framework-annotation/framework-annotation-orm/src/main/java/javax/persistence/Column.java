package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface Column {

    String name() default "";

    boolean unique() default false;

    boolean nullable() default true;

    boolean insertable() default true;

    boolean updatable() default true;

    String columnDefinition() default "";

    String table() default "";

    /**
     * 只在字符串数据类型时使用
     * @return 长度
     */
    int length() default 255;

    /**
     * 数字小数部分
     * @return 数字小数部分精度
     */
    int precision() default 0;

    /**
     * 整数部分
     * @return 整数部分精度
     */
    int scale() default 0;
}
