package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.rnkrsoft.framework.orm.SupportedJdbcType;
import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.utils.StringUtils;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
import com.rnkrsoft.framework.toolkit.generator.MapperGenerator;

/**
 * Created by rnkrsoft.com on 2018/5/6.
 */
public class JdkMapperGenerator extends JdkGenerator implements MapperGenerator {
    @Override
    public ByteBuf generate(GenerateContext ctx) {
        ByteBuf buf = ByteBuf.allocate(1024).autoExpand(true);
        TableMetadata metadata = ctx.getTableMetadata();
        buf.put("UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", "\n");
        buf.put("UTF-8", "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >", "\n");
        buf.put("UTF-8", MessageFormatter.format("<mapper namespace=\"{}\" >", metadata.getDaoClassName()), "\n");

        buf.put("UTF-8", indent(), MessageFormatter.format("<resultMap id=\"BaseResultMap\" type=\"{}\">", metadata.getEntityClassName()), "\n");
        for (String column : metadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = metadata.getColumnMetadataSet().get(column);
            if (columnMetadata.getComment() != null && !columnMetadata.getComment().isEmpty()) {
                buf.put("UTF-8", indent(), indent(), "<!-- ", columnMetadata.getComment(), " -->\n");
            }
            String str = "";
            if (metadata.getPrimaryKeys().contains(column)) {
                str = "<id ";
            } else {
                str = "<result ";
            }
            str += MessageFormatter.format("column=\"{}\" property=\"{}\" jdbcType=\"{}\" />", columnMetadata.getJdbcName(), columnMetadata.getJavaName(), getJdbcType(columnMetadata.getJdbcType()));
            buf.put("UTF-8", indent(), indent(), str, "\n");
        }
        buf.put("UTF-8", indent(), "</resultMap>", "\n");
        buf.put("UTF-8", indent(), "<sql id=\"Base_Column_List\" >", "\n");
        buf.put("UTF-8", indent(), indent());
        int columnSize = metadata.getOrderColumns().size();
        int columnIndex = 0;
        for (String column : metadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = metadata.getColumnMetadataSet().get(column);
            buf.put("UTF-8", columnMetadata.getJdbcName());
            columnIndex++;
            if (columnIndex != columnSize) {
                buf.put("UTF-8", ", ");
            }
        }
        buf.put("UTF-8", "\n");
        buf.put("UTF-8", indent(), "</sql>", "\n");
        buf.put("UTF-8", "</mapper>");
        return buf;
    }

    String getJdbcType(SupportedJdbcType dataType){
        if (dataType == SupportedJdbcType.INT){
            return SupportedJdbcType.INTEGER.getCode();
        }else{
            return dataType.getCode();
        }
    }
}
