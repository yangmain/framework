package com.rnkrsoft.framework.config.client;

import com.rnkrsoft.framework.config.utils.FileSystemUtils;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.FileObject;
import com.rnkrsoft.framework.config.v1.ParamObject;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@ToString
@Getter
class ConfigCache {
    /**
     * 缓存的上一次应答对象
     */
    FetchResponse response;
    /**
     * 配置文件列表
     */
    final Map<String, FileObject> files = new HashMap();
    /**
     * 配置参数列表
     */
    final Map<String, ParamObject> params = new HashMap();

    public ConfigCache(FetchResponse response) {
        this.response = response;
        for (ParamObject paramObject : response.getParams()) {
            params.put(paramObject.getKey(), paramObject);
        }
        for (FileObject fileObject : response.getFiles()) {
            String fileKey = fileObject.getFilePath() + File.separator + fileObject.getFileName();
            files.put(FileSystemUtils.formatPath(fileKey), fileObject);
        }
    }

    public FileObject getFile(String fileName){
        return files.get(fileName);
    }

    public ParamObject getParam(String paramName){
        return params.get(paramName);
    }
}
