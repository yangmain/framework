package com.rnkrsoft.framework.orm.config;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 */

import com.rnkrsoft.framework.orm.NameMode;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemConfig {
    /**
     * 数据库模式模式
     * @return
     */
    NameMode schemaMode = NameMode.auto;
    /**
     * 前缀模式
     */
    NameMode prefixMode = NameMode.auto;
    /**
     * 后缀模式
     */
    NameMode suffixMode = NameMode.auto;
    /**
     * 表模式
     */
    String schema;
    /**
     * 表前缀
     */
    String prefix;
    /**
     * 表后缀
     */
    String suffix;
    /**
     * 自动创建表
     */
    boolean autoCreateTable = false;
}

