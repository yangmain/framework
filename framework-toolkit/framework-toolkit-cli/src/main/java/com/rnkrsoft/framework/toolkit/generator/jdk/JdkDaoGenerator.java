package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.devops4j.io.buffer.ByteBuf;
import com.devops4j.message.MessageFormatter;
import com.devops4j.utils.StringUtils;
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
        buf.put("UTF-8", MessageFormatter.format("package {}.dao;", ctx.getPackageName()), "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.CrudMapper;", "\n");
        buf.put("UTF-8", "import ", metadata.getEntityClassName(),";", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "/**", "\n");
        buf.put("UTF-8", " * 版权归氡氪网络科技有限公司所有 rnkrsoft.com 框架自动生成!", "\n");
        buf.put("UTF-8", " */", "\n");
        buf.put("UTF-8", MessageFormatter.format("public interface {} extends CrudMapper<{}, String>{", daoName, entityName), "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "}", "\n");
        return buf;
    }
}
