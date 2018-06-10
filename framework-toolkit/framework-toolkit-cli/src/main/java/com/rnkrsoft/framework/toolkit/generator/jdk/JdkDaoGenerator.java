package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.utils.StringUtils;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.generator.DaoGenerator;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;

/**
 * Created by rnkrsoft.com on 2018/4/26.
 */
public class JdkDaoGenerator extends JdkGenerator implements DaoGenerator {

    @Override
    public ByteBuf generate(GenerateContext ctx) {
        ByteBuf buf = ByteBuf.allocate(1024).autoExpand(true);
        TableMetadata metadata = ctx.getTableMetadata();
        String entityName = StringUtils.firstCharToUpper(StringUtils.underlineToCamel(metadata.getTableName())) + "Entity";
        String daoName = StringUtils.firstCharToUpper(StringUtils.underlineToCamel(metadata.getTableName())) + "DAO";
        String pkName = metadata.getPrimaryKeys().get(0);
        ColumnMetadata columnMetadata = metadata.getColumnMetadataSet().get(pkName);
        buf.put("UTF-8", MessageFormatter.format("package {}.dao;", ctx.getPackageName()), "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;", "\n");
        buf.put("UTF-8", "import ", metadata.getEntityClassName(),";", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "/**", "\n");
        buf.put("UTF-8", " * rnkrsoft.com 框架自动生成!", "\n");
        buf.put("UTF-8", " */", "\n");
        buf.put("UTF-8", MessageFormatter.format("public interface {} extends JdbcMapper<{}, {}>{", daoName, entityName, columnMetadata.getJavaType().getSimpleName()), "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "}", "\n");
        return buf;
    }
}
