package com.rnkrsoft.framework.orm.mysql;

/**
 * 存储引擎类型<br>
 *     <ol>
 *         <li>InnoDB存储引擎</li>
 *         <li>MyISAM存储引擎</li>
 *         <li>MySQL自动选择</li>
 *     </ol>
 */
public enum DataEngineType {
    InnoDB,
    MyISAM,
    AUTO
}
