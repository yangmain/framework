package com.rnkrsoft.framework.test.junit.listener.createtable;

import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/4/30.
 */
@Data
@Builder
@ToString
public class CreateTableWrapper {
    CreateTableScope scope = CreateTableScope.Method;
    /**
     * 实体类
     */
    Class entityClass;
    TableMetadata metadata;
    /**
     * 关键字模式
     */
    WordMode keywordMode = WordMode.lowerCase;
    /**
     * SQL模式
     */
    WordMode sqlMode = WordMode.lowerCase;
    /**
     * 数据库模式类型
     */
    NameMode schemaMode = NameMode.customize;
    /**
     * 数据库模式
     */
    String schema;
    /**
     * 前缀模式
     */
    NameMode prefixMode = NameMode.customize;
    /**
     * 前缀
     */
    String prefix;
    /**
     * 后缀模式
     */
    NameMode suffixMode = NameMode.customize;
    /**
     * 后缀
     */
    String suffix;
    /**
     * 测试前创建表结构
     */
    boolean createBeforeTest = true;
    /**
     * 创建表结构前删除表结构
     */
    boolean dropBeforeCreate = true;
    /**
     * 测试后删除表结构
     */
    boolean dropAfterTest = true;
}
