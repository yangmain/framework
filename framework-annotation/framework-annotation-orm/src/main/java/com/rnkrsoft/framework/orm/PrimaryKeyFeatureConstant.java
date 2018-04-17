package com.rnkrsoft.framework.orm;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 主键生成特征字符串常量
 */
public interface PrimaryKeyFeatureConstant {
    String YYYY_MM_DD_HH_MM_SS_SSS = "xxx${yyyyMMddHHmmssSSS}${seqNo:5}yyy";
}
