package com.rnkrsoft.framework.config.client;

import com.rnkrsoft.framework.config.security.AES;
import com.rnkrsoft.framework.config.utils.FileSystemUtils;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.FileObject;
import com.rnkrsoft.framework.config.v1.ParamObject;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@ToString
@Getter
@Slf4j
class ConfigCache {
    ConfigClientSetting setting;
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
    /**
     * 最后变动时间戳，如果客户端保存的上次变更与当次大于或者等于，则不进行参数处理操作
     */
    long lastUpdateTimestamp;

    public ConfigCache(FetchResponse response,  ConfigClientSetting setting) {
        this.response = response;
        for (ParamObject paramObject : response.getParams()) {
            String val = paramObject.getValue();
            if (paramObject.isEncrypt()){
                try {
                    val = val.substring("AES/BASE64://".length());
                    val = AES.decrypt(setting.getSecurityKey(), AES.DEFAULT_IV, val);
                } catch (Exception e) {
                    log.error("解密参数 '{}'发生失败!", paramObject.getKey());
                }
            }
            paramObject.setValue(val);
            params.put(paramObject.getKey(), paramObject);
        }
        for (FileObject fileObject : response.getFiles()) {
            String fileKey = fileObject.getFileFullName();
            files.put(FileSystemUtils.formatPath(fileKey), fileObject);
        }
        this.lastUpdateTimestamp = response.getUpdateTimestamp();
    }

    public FileObject getFile(String fileName){
        return files.get(fileName);
    }

    public ParamObject getParam(String paramName){
        return params.get(paramName);
    }
}
