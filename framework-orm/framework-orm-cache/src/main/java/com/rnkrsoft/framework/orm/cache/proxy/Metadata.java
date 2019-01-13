package com.rnkrsoft.framework.orm.cache.proxy;

import lombok.Data;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Data
public class Metadata {
    CommandType commandType;
    int expire;
    int decrement;
    int increment;
    int seconds;
}
