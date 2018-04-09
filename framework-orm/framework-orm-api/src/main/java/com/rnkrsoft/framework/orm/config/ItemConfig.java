package com.rnkrsoft.framework.orm.config;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 */

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemConfig {
    /**
     * 是否使用表模式
     */
    boolean useSchema = true;
    /**
     * 使用实体表模式
     */
    boolean useEntitySchema = true;
    /**
     * 表模式
     */
    String schema;
    /**
     * 使用实体前缀
     */
    boolean useEntityPrefix = true;
    /**
     * 表前缀
     */
    String prefix;
    /**
     * 使用实体后缀
     */
    boolean useEntitySuffix = true;
    /**
     * 表后缀
     */
    String suffix;
    /**
     * 自动创建表
     */
    boolean autoCreateTable = false;
}

