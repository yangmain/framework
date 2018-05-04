package com.rnkrsoft.framework.orm.mysql;

import com.rnkrsoft.framework.orm.DatabaseType;
import lombok.Getter;

/**
 * 存储引擎类型<br>
 *     <ol>
 *         <li>InnoDB存储引擎</li>
 *         <li>MyISAM存储引擎</li>
 *         <li>MySQL自动选择</li>
 *     </ol>
 */
@Getter
public enum DataEngineType{
    InnoDB("innoDB"),
    MyISAM("MyISAM"),
    AUTO("");
    String value;
    DataEngineType( String value){
        this.value = value;
    }

}
