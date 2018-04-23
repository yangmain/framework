package com.rnkrsoft.framework.toolkit.generator;

import com.devops4j.io.buffer.ByteBuf;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 */
public interface Generator {
    /**
     * 根据表名生成代码
     * @param ctx 上下文
     * @return 字节码
     */
    ByteBuf generate(GenerateContext ctx);
}
