package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.rnkrsoft.framework.orm.SupportedJdbcType;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.generator.EntityGenerator;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.utils.StringUtils;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 */
public class JdkEntityGenerator extends JdkGenerator implements EntityGenerator {
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
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.PrimaryKey;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.Table;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.Comment;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.StringColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.NumberColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.DateColumn;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.StringType;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.NumberType;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.DateType;", "\n");
        buf.put("UTF-8", "import com.rnkrsoft.framework.orm.jdbc.OrderByEntity;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "import lombok.Data;", "\n");
        buf.put("UTF-8", "import lombok.Builder;", "\n");
        buf.put("UTF-8", "import lombok.NoArgsConstructor;", "\n");
        buf.put("UTF-8", "import lombok.AllArgsConstructor;", "\n");
        buf.put("UTF-8", "import lombok.ToString;", "\n");
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", "/**", "\n");
        buf.put("UTF-8", " * rnkrsoft.com 框架自动生成!", "\n");
        buf.put("UTF-8", " */", "\n");
        buf.put("UTF-8", "@Data", "\n");
        buf.put("UTF-8", "@Builder", "\n");
        buf.put("UTF-8", "@NoArgsConstructor", "\n");
        buf.put("UTF-8", "@AllArgsConstructor", "\n");
        buf.put("UTF-8", "@ToString", "\n");
        buf.put("UTF-8", MessageFormatter.format("@Table(name = \"{}\", prefix = \"{}\", suffix = \"{}\")", metadata.getTableName(), metadata.getPrefix(), metadata.getSuffix()), "\n");
        buf.put("UTF-8", MessageFormatter.format("@Comment(\"{}\")", metadata.getComment()), "\n");
        buf.put("UTF-8", MessageFormatter.format("public class {} extends OrderByEntity implements Serializable {", entityName), "\n");
        for (String columnName : metadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = metadata.getColumnMetadataSet().get(columnName);
            buf.put("UTF-8", indent(), MessageFormatter.format("@Comment(\"{}\")", columnMetadata.getComment()), "\n");
            if (metadata.getPrimaryKeys().contains(columnName)) {
                String str = indent() + "@PrimaryKey(";
                if ((columnMetadata.getJdbcType() == SupportedJdbcType.TINYINT
                        || columnMetadata.getJdbcType() == SupportedJdbcType.SMALLINT
                        || columnMetadata.getJdbcType() == SupportedJdbcType.INTEGER
                        || columnMetadata.getJdbcType() == SupportedJdbcType.INT
                        || columnMetadata.getJdbcType() == SupportedJdbcType.BIGINT
                ) && columnMetadata.getAutoIncrement() != null && columnMetadata.getAutoIncrement()) {
                    str += "strategy = PrimaryKeyStrategy.IDENTITY";
                } else if (columnMetadata.getJdbcType() == SupportedJdbcType.VARCHAR) {
                    str += "strategy = PrimaryKeyStrategy.UUID";
                } else if (columnMetadata.getJdbcType() == SupportedJdbcType.CHAR) {
                    str += "strategy = PrimaryKeyStrategy.UUID";
                } else {

                }
                str += ")";
                buf.put("UTF-8", str, "\n");
            }
            if (columnMetadata.getJdbcType() == SupportedJdbcType.VARCHAR) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.VARCHAR)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.CHAR) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.CHAR)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.LONGVARCHAR) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.TEXT)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.BIGINT) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.LONG)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Long {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.INTEGER || columnMetadata.getJdbcType() == SupportedJdbcType.INT) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.INTEGER)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Integer {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.SMALLINT) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.SHORT)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Integer {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.TINYINT) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.BYTE)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Integer {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.DECIMAL) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@NumberColumn(name = \"{}\", nullable = {}, type = NumberType.DECIMAL )", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("BigDecimal {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.DATE) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@DateColumn(name = \"{}\", nullable = false, type = DateType.DATE)", columnMetadata.getJdbcName()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Date {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.DATETIME) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@DateColumn(name = \"{}\", nullable = false, type = DateType.DATE)", columnMetadata.getJdbcName()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Date {};", columnMetadata.getJavaName()));
            } else if (columnMetadata.getJdbcType() == SupportedJdbcType.TIMESTAMP) {
                buf.put("UTF-8", indent(), MessageFormatter.format("@DateColumn(name = \"{}\", nullable = false, type = DateType.TIMESTAMP)", columnMetadata.getJdbcName()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("Date {};", columnMetadata.getJavaName()));
            } else {
                buf.put("UTF-8", indent(), MessageFormatter.format("@StringColumn(name = \"{}\", nullable = {}, type = StringType.VARCHAR)", columnMetadata.getJdbcName(), columnMetadata.getNullable()), "\n");
                buf.put("UTF-8", indent(), MessageFormatter.format("String {};", columnMetadata.getJavaName()));
            }
            buf.put("UTF-8", "\n");
            buf.put("UTF-8", "\n");
        }
        buf.put("UTF-8", "}", "\n");
        return buf;
    }
}
