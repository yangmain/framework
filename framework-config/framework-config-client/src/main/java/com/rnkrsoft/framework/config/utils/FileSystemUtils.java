package com.rnkrsoft.framework.config.utils;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
public class FileSystemUtils {
    /**
     * 格式化路径
     * @param path 路径
     * @return 路径
     */
    public static String formatPath(String path){
        String path0 =  path.replaceAll("\\\\", "/").replaceAll("//","/");
        return path0;
    }
}
