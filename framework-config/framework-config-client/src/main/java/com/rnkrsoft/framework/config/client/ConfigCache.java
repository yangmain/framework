package com.rnkrsoft.framework.config.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.config.security.AES;
import com.rnkrsoft.framework.config.utils.FileSystemUtils;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.FileObject;
import com.rnkrsoft.framework.config.v1.ParamObject;
import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.io.file.DynamicFile;
import com.rnkrsoft.io.file.FileTransaction;
import com.rnkrsoft.io.file.FileWrapper;
import com.rnkrsoft.message.MessageFormatter;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 * 配置缓存对象，用于对远程获取配置进行转换，存储在本地内存中
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

    final static Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();

    public ConfigCache(FetchResponse response, ConfigClientSetting setting) {
        this.response = response;
        this.setting = setting;
        for (ParamObject paramObject : response.getParams()) {
            String val = paramObject.getValue();
            if (paramObject.isEncrypt()) {
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
        this.response.setId(null);
        this.response.setUpdateTimestamp(0);
    }

    public FileObject getFile(String fileName) {
        return files.get(fileName);
    }

    public ParamObject getParam(String paramName) {
        return params.get(paramName);
    }

    public static ConfigCache load(ConfigClientSetting setting) throws IOException {
        DynamicFile file = DynamicFile.file(MessageFormatter.format("{}/{}/{}/{}/{}/{}.json", setting.getWorkHome(), setting.getGroupId(), setting.getArtifactId(), setting.getVersion(), setting.getEnv(), setting.getMachine()), 5);
        FileWrapper fileWrapper = file.getFile();
        ByteBuf byteBuf = fileWrapper.read();
        String json = byteBuf.asString("UTF-8");
        FetchResponse response = GSON.fromJson(json, FetchResponse.class);
        return new ConfigCache(response, setting);
    }

    public void save() throws IOException {
        DynamicFile file = DynamicFile.file(MessageFormatter.format("{}/{}/{}/{}/{}/{}.json", setting.getWorkHome(), setting.getGroupId(), setting.getArtifactId(), setting.getVersion(), setting.getEnv(), setting.getMachine()), 5);
        FileTransaction fileTransaction = file.begin();
        try {
            String json = GSON.toJson(response);
            fileTransaction.write(json);
            fileTransaction.commit();
        } catch (Exception e) {
            log.error("持久化发生错误", e);
            fileTransaction.rollback();
        }
        file.lookupMaxVersion();
    }
}
