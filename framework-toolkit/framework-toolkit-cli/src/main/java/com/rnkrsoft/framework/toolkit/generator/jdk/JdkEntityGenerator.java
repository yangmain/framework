package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.devops4j.io.buffer.ByteBuf;
import com.devops4j.message.MessageFormatter;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.generator.EntityGenerator;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 */
public class JdkEntityGenerator extends JdkGenerator implements EntityGenerator{
    @Override
    public ByteBuf generate(GenerateContext ctx) {
        ByteBuf buf = ByteBuf.allocate(1024).autoExpand(true);
        TableMetadata metadata = ctx.getTableMetadata();
        String entityName = StringUtils.firstCharToUpper(StringUtils.underlineToCamel(metadata.getTableName())) + "Entity";
        buf.put("UTF-8", MessageFormatter.format("package {}.entity;", ctx.getPackageName()), "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import java.io.Serializable;", "\n");
        buf.put("UTF-8", "import java.math.BigDecimal;", "\n");
        buf.put("UTF-8", "import java.util.Date;", "\n");
        buf.put("UTF-8", "import java.sql.Timestamp;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.Table;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.Comment;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.PrimaryKey;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.StringColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.NumberColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.DateColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.StringType;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.NumberType;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.DateType;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import lombok.Data;", "\n");
        buf.put("UTF-8", "import lombok.Builder;", "\n");
        buf.put("UTF-8", "import lombok.NoArgsConstructor;", "\n");
        buf.put("UTF-8", "import lombok.AllArgsConstructor;", "\n");
        buf.put("UTF-8", "import lombok.ToString;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "/**", "\n");
        buf.put("UTF-8", " * 版权归氡氪网络科技有限公司所有 rnkrsoft.com 框架自动生成!", "\n");
        buf.put("UTF-8", " */", "\n");
        buf.put("UTF-8", "@Data", "\n");
        buf.put("UTF-8", "@Builder", "\n");
        buf.put("UTF-8", "@NoArgsConstructor", "\n");
        buf.put("UTF-8", "@AllArgsConstructor", "\n");
        buf.put("UTF-8", "@ToString", "\n");
        buf.put("UTF-8", MessageFormatter.format("@Table(name = \"{}\")", metadata.getTableName()), "\n");
        buf.put("UTF-8", MessageFormatter.format("@Comment(\"{}\")", metadata.getComment()), "\n");
        buf.put("UTF-8", MessageFormatter.format("public class {} implements Serializable {", entityName), "\n");
//        buf.put("UTF-8", "\n");
        for (String columnName : metadata.getOrderColumns()){
            ColumnMetadata columnMetadata = metadata.getColumnMetadataSet().get(columnName);
            buf.put("UTF-8", indent(), MessageFormatter.format("@Comment(\"{}\")", columnMetadata.getComment()), "\n");
            if(metadata.getPrimaryKeys().contains(columnName)){
                String str = indent() + "@PrimaryKey(";
                if(columnMetadata.getJdbcType().equals("NUMERIC") && columnMetadata.getAutoIncrement() != null && columnMetadata.getAutoIncrement()){
                    str += "strategy = PrimaryKeyStrategy.IDENTITY";
                }else if(columnMetadata.getJdbcType().equals("VARCHAR")){
                    str += "strategy = PrimaryKeyStrategy.UUID";
                }else if(columnMetadata.getJdbcType().equals("CHAR")){
                    str += "strategy = PrimaryKeyStrategy.UUID";
                }else{

                }
                str +=")";
                buf.put("UTF-8", str, "\n");
            }
            if (columnMetadata.getJdbcType().equals("VARCHAR")){
                buf.put("UTF-8", indent(), MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.VARCHAR)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("CHAR")){
                buf.put("UTF-8",indent(), MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.CHAR))", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8",indent(), MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("NUMERIC")){
                buf.put("UTF-8", indent(),MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.INTEGER)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("Integer {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("DECIMAL")){
                buf.put("UTF-8", indent(),MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.DECIMAL )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("BigDecimal {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("DATE")){
                buf.put("UTF-8", indent(),MessageFormatter.format("@DateColumn(name = \"{}\", nullable = {}, type = DateType.DATE)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("Date {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("DATETIME")){
                buf.put("UTF-8", indent(),MessageFormatter.format("@DateColumn(name = \"{}\", nullable = {}, type = DateType.DATE)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("Date {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("TIMESTAMP")){
                buf.put("UTF-8", indent(),MessageFormatter.format("@DateColumn(name = \"{}\", nullable = {}, type = DateType.TIMESTAMP)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("Timestamp {};", columnMetadata.getJavaName()));
            }else{
                buf.put("UTF-8", indent(),MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.VARCHAR)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(),MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            }
            buf.put("UTF-8", "\n");
            buf.put("UTF-8", "\n");
        }
        buf.put("UTF-8", "}", "\n");
        return buf;
    }
}
