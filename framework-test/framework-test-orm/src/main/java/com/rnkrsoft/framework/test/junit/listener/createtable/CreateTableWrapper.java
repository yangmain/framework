package com.rnkrsoft.framework.test.junit.listener.createtable;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;
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
     * 数据库模式
     */
    NameMode schemaMode = NameMode.customize;
    String schema;
    NameMode prefixMode = NameMode.customize;
    String prefix;
    NameMode suffixMode = NameMode.customize;
    String suffix;
    boolean createBeforeTest = true;
    boolean testBeforeDrop = true;
}
