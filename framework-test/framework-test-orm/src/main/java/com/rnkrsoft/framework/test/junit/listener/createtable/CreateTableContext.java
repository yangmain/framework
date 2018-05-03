package com.rnkrsoft.framework.test.junit.listener.createtable;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/4/30.
 */
@Data
public class CreateTableContext {
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
    /**
     * 测试前创建表结构
     */
    boolean createBeforeTest = true;
    /**
     * 创建表结构前删除表结构
     */
    boolean dropBeforeCreate = true;
    boolean dropAfterTest = true;
    /**
     * 键为表名，值为封装
     */
    final Map<String, CreateTableWrapper> tables = new HashMap();

    final static ThreadLocal<CreateTableContext> CONTEXT = new ThreadLocal();

    public void addTable(CreateTableWrapper table){
        EntityExtractorHelper helper = new EntityExtractorHelper();
        TableMetadata metadata = helper.extractTable(table.entityClass, false);
        table.setMetadata(metadata);
        if (!tables.containsKey(metadata.getTableName())){
            metadata.setDataEngine(null);
            metadata.setPrefix(table.getPrefix());
            metadata.setSuffix(table.getSuffix());
            metadata.setSchema(null);
            tables.put(metadata.getTableName().toUpperCase(), table);
        }

    }
    public static CreateTableContext create(){
        CreateTableContext ctx = new CreateTableContext();
        CONTEXT.set(ctx);
        return ctx;
    }

    public static CreateTableContext context(){
        CreateTableContext ctx = CONTEXT.get();
        if (ctx == null){
            ctx = create();
        }
        return ctx;
    }
}