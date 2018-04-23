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
public class EntityGeneratorJdk implements EntityGenerator{
    @Override
    public ByteBuf generate(GenerateContext ctx) {
        ByteBuf buf = ByteBuf.allocate(1024).autoExpand(true);
        TableMetadata metadata = ctx.getTableMetadata();
        buf.put("UTF-8", MessageFormatter.format("package {};", ctx.getPackageName()), "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import java.io.Serializable;", "\n");
        buf.put("UTF-8", "import java.math.BigDecimal;", "\n");
        buf.put("UTF-8", "import java.util.Date;", "\n");
        buf.put("UTF-8", "import java.sql.Timestamp;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.Table;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.Comment;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.PrimaryKey;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.StringColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.NumberColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.DateColumn;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "/**", "\n");
        buf.put("UTF-8", " * created by rnkrsoft.com ORM framework auto generate!", "\n");
        buf.put("UTF-8", " */", "\n");
        buf.put("UTF-8", MessageFormatter.format("public class {} implements Serializable{", StringUtils.firstCharToUpper(StringUtils.underlineToCamel(metadata.getTableName()))), "\n");
        buf.put("UTF-8", "\n");
        for (String columnName : metadata.getOrderColumns()){
            ColumnMetadata columnMetadata = metadata.getColumnMetadataSet().get(columnName);
            buf.put("UTF-8", MessageFormatter.format("  @Comment(\"{}\")", columnMetadata.getComment()), "\n");
            if(metadata.getPrimaryKeys().contains(columnName)){
                String str = "  @PrimaryKey(";
                if(columnMetadata.getJdbcType().equals("NUMERIC") && columnMetadata.getAutoIncrement() != null && columnMetadata.getAutoIncrement()){
                    str += "strategy = PrimaryKeyStrategy.IDENTITY ";
                }else if(columnMetadata.getJdbcType().equals("VARCHAR")){
                    str += "strategy = PrimaryKeyStrategy.UUID ";
                }else if(columnMetadata.getJdbcType().equals("CHAR")){
                    str += "strategy = PrimaryKeyStrategy.UUID ";
                }else{

                }
                str +=")";
                buf.put("UTF-8", str, "\n");
            }
            if (columnMetadata.getJdbcType().equals("VARCHAR")){
                buf.put("UTF-8", MessageFormatter.format("  @StringColumn(name = \"{}\", nullable={})", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  String {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("CHAR")){
                buf.put("UTF-8", MessageFormatter.format("  @StringColumn(name = \"{}\", nullable={}))", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  String {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("NUMERIC")){
                buf.put("UTF-8", MessageFormatter.format("  @NumberColumn(name = \"{}\", nullable= {} )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  Integer {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("DECIMAL")){
                buf.put("UTF-8", MessageFormatter.format("  @NumberColumn(name = \"{}\", nullable= {} )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  BigDecimal {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("DATE")){
                buf.put("UTF-8", MessageFormatter.format("  @DateColumn(name = \"{}\", nullable= {} )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  Date {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("DATETIME")){
                buf.put("UTF-8", MessageFormatter.format("  @DateColumn(name = \"{}\", nullable= {} )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  Date {};", columnMetadata.getJavaName()));
            }else if(columnMetadata.getJdbcType().equals("TIMESTAMP")){
                buf.put("UTF-8", MessageFormatter.format("  @DateColumn(name = \"{}\", nullable= {} )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", MessageFormatter.format("  Timestamp {};", columnMetadata.getJavaName()));
            }
            buf.put("UTF-8", "\n");
            buf.put("UTF-8", "\n");
        }
        buf.put("UTF-8", "}", "\n");
        return buf;
    }
}
