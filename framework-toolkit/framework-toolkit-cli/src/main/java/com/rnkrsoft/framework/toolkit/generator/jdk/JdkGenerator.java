package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.rnkrsoft.framework.toolkit.generator.Generator;

/**
 * Created by rnkrsoft.com on 2018/4/29.
 */
public abstract class JdkGenerator implements Generator{
    /**
     * 输出一个缩进
     * @param space 是否为空格替换
     * @return 缩进量
     */
    protected String indent(boolean space){
        return space ? "    " : "\t";
    }
    protected String indent(){
        return indent(true);
    }
}
