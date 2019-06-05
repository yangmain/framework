package com.rnkrsoft.framework.config.v1;

import lombok.*;

import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 参数对象
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamObject {
    /**
     * 参数名
     */
    String key;
    /**
     * 参数值
     */
    String value;
    /**
     * 参数类型
     */
    int type = ParamType.SYSTEM.code;
    /**
     * 数据类型
     */
    int dataType = DataTypeEnum.STRING.code;
    /**
     * 参数描述
     */
    String desc;
    /**
     * 是否动态参数
     */
    boolean dynamic = false;
    /**
     * 是否启用该参数
     */
    boolean enabled = false;
    /**
     * 是否复制到系统属性中
     */
    boolean systemProperties = false;
    /**
     * 是否加密
     */
    boolean encrypt = false;
    /**
     * 创建人
     */
    String createUid;

    /**
     * 最后更新人
     */
    String lastUpdateUid;

    /**
     * 创建时间
     */
    String createTime;

    /**
     * 修改时间
     */
    String updateTime;

    public void setParamType(ParamType paramType){
        this.type = paramType.code;
    }

    public ParamType getParamType(){
        return ParamType.valueOfCode(this.type);
    }


}
