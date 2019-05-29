package com.rnkrsoft.framework.config.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.config.connector.Connector;
import com.rnkrsoft.framework.config.connector.HttpConnector;
import com.rnkrsoft.framework.config.security.AES;
import com.rnkrsoft.framework.config.utils.FileSystemUtils;
import com.rnkrsoft.framework.config.utils.Http;
import com.rnkrsoft.framework.config.utils.PropertiesUtils;
import com.rnkrsoft.framework.config.utils.ValueUtils;
import com.rnkrsoft.framework.config.v1.*;
import com.rnkrsoft.io.file.DynamicFile;
import com.rnkrsoft.io.file.FileTransaction;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.message.MessageFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 配置客户端
 */
@Slf4j
public class ConfigClient {
    /**
     * 客户端配置对象
     */
    ConfigClientSetting setting;
    /**
     * 定时器
     */
    Timer timer;
    /**
     * 运行时的属性
     */
    final Properties runtimeProperties = new Properties();

    /**
     * 连接器
     */
    Connector connector;

    /**
     * 文件分发复制线程池
     */
    ExecutorService fileCopyPool;
    /**
     * 配置客户端
     */
    static ConfigClient CONFIG_CLIENT = new ConfigClient();
    /**
     * 配置信息缓存
     */
    static ConfigCache CONFIG_CACHE;

    /**
     * 获取客户端
     *
     * @return 返回单例
     */
    public static ConfigClient getInstance() {
        return CONFIG_CLIENT;
    }

    static Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * 从服务器拿配置信息
     */
    static class FetchConfigTask extends TimerTask {
        ConfigClient configClient;

        public FetchConfigTask(ConfigClient configClient) {
            this.configClient = configClient;
        }

        @Override
        public void run() {
            this.configClient.fetch();
        }
    }

    /**
     * 初始化客户端
     *
     * @param setting 配置对象
     * @return 初始化后的客户端
     */
    public ConfigClient init(ConfigClientSetting setting) {
        synchronized (this) {
            this.setting = setting;
            log.info("begin to init config client...");
            if (this.timer != null) {
                this.timer.cancel();
            }
            this.timer = new Timer("config-center-timer-fetch", true);
            this.runtimeProperties.clear();
            if (this.connector != null) {
                this.connector.stop();
                this.connector = null;
            }
            if (setting.connectorType == ConnectorType.HTTP) {
                //初始化HTTP连接器
                this.connector = new HttpConnector(setting.host, setting.port);
                this.connector.start();
                //初始化定时器，每隔一段事件主动拉去一次配置
                this.timer.schedule(new FetchConfigTask(this), setting.fetchIntervalSeconds * 1000, setting.fetchIntervalSeconds * 1000);
                fetch();
            } else if (setting.connectorType == ConnectorType.DUBBO) {
                //初始化 DUBBO的服务
            } else {
                throw new Error(MessageFormatter.format("not supported connectorType!"));
            }
            log.info("finish inite config client...");
        }
        return this;
    }

    /**
     * 拉取中心服务器的参数和文件信息
     */
    public void fetch() {
        //1.调用连接器的拉取
        FetchRequest request = FetchRequest.builder()
                .id(UUID.randomUUID().toString())
                .groupId(setting.groupId)
                .artifactId(setting.artifactId)
                .version(setting.version)
                .env(setting.env)
                .machine(setting.machine)
                .build();
        FetchResponse response = connector.fetch(request);
        if (ResponseCode.valueOfCode(response.getRspCode()) != ResponseCode.SUCCESS) {
            log.error("fetch config happens error!");
            return;
        }
        if (CONFIG_CACHE != null && CONFIG_CACHE.getLastUpdateTimestamp() >= response.getUpdateTimestamp()) {
            log.info("no change!");
            return;
        }
        Map<String, ParamObject> oldParams = null;
        Map<String, FileObject> oldFiles = null;
        if (CONFIG_CACHE != null) {
            oldParams = CONFIG_CACHE.getParams();
            oldFiles = CONFIG_CACHE.getFiles();
        } else {
            oldParams = new HashMap();
            oldFiles = new HashMap();
        }
        ConfigCache configCache = new ConfigCache(response, setting);
        Map<String, ParamObject> newParams = configCache.getParams();
        Map<String, FileObject> newFiles = configCache.getFiles();
        Properties tempProperties = PropertiesUtils.clone(runtimeProperties);
        //对删除的参数进行处理
        for (String oldKey : oldParams.keySet()) {
            if (newParams.containsKey(oldKey)) {

            } else {
                if (setting.printLog) {
                    log.info("delete '{}' in runtimeProperties", oldKey);
                }
                //如果没有发现，说明删除了
                tempProperties.remove(oldKey);
                if (System.getProperties().containsKey(oldKey)) {
                    System.getProperties().remove(oldKey);
                }
            }

        }
        //对新增，更新参数进行处理
        for (ParamObject newParamObject : newParams.values()) {
            //原来有，现在也有为更新
            if (oldParams.containsKey(newParamObject.getKey())) {
                if (setting.printLog) {
                    log.info("update '{}' in runtimeProperties, value:{}", newParamObject.getKey(), newParamObject.getValue());
                }
                String val = newParamObject.getValue();
                if (tempProperties.containsKey(newParamObject.getKey())) {
                    tempProperties.remove(newParamObject.getKey());
                }
                tempProperties.setProperty(newParamObject.getKey(), val);
                if (newParamObject.isSystemProperties()) {
                    if (System.getProperties().containsKey(newParamObject.getKey())) {
                        System.getProperties().remove(newParamObject.getKey());
                    }
                    System.getProperties().setProperty(newParamObject.getKey(), val);
                }
                ParamObject oldParamObject = oldParams.get(newParamObject.getKey());
                newParamObject.setDynamic(oldParamObject.isDynamic());
            } else {//原来没有，现在有为新增
                if (setting.printLog) {
                    log.info("add '{}' in runtimeProperties, value:{}", newParamObject.getKey(), newParamObject.getValue());
                }
                String val = newParamObject.getValue();
                tempProperties.setProperty(newParamObject.getKey(), val);
                if (newParamObject.isSystemProperties()) {
                    System.getProperties().setProperty(newParamObject.getKey(), val);
                }
                newParamObject.setDynamic(false);
            }
        }
        //2.拉取后异步下载文件到本地
        //对删除的文件进行处理
        for (String oldKey : oldFiles.keySet()) {
            if (oldFiles.containsKey(oldKey)) {

            } else {
                if (setting.printLog) {
                    log.info("delete '{}' file", oldKey);
                }
            }

        }
        //对新增和修改
        for (String newKey : newFiles.keySet()) {
            FileObject newFileObject = newFiles.get(newKey);
            String oldFileFingerprint = null;
            if (oldFiles.containsKey(newKey)) {//更新
                if (setting.printLog) {
                    log.info("modify '{}' file", newKey);
                }
                oldFileFingerprint = oldFiles.get(newKey).getFileFingerprint();
            } else {//新增
                if (setting.printLog) {
                    log.info("create '{}' file", newKey);
                }
            }
            if (!newFileObject.isLazyDownload()) {
                //下载
                if (newFileObject.getTransferType() == FileTransferType.HTTP) {
                    final String oldFileFingerprint0 = oldFileFingerprint;
                    final FileObject newFileObject0 = newFileObject;
                    final String fileName0 = newKey;
                    fileCopyPool.submit(new Runnable() {
                        public void run() {
                            try {
                                downloadFile(newFileObject0, oldFileFingerprint0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (setting.printLog) {
                                log.info("http download file  " + fileName0 + " success!");
                            }
                        }
                    });

                } else {
                    log.error("'{}'not support transfer Type '{}'", newFileObject.getFileFullName(), newFileObject.getTransferType());
                }
            }
        }
        //防止并发问题
        synchronized (this) {
            runtimeProperties.clear();
            runtimeProperties.putAll(tempProperties);
        }
        CONFIG_CACHE = configCache;
        //预置打印日志的属性
        this.setting.printLog = getProperty("system.config.log.print", "true", Boolean.TYPE);

    }

    /**
     * 下载一个指定的文件
     *
     * @param fileObject         文件对象
     * @param oldFileFingerprint 文件签字
     */
    public void downloadFile(FileObject fileObject, String oldFileFingerprint) throws IOException {
        String path0 = FileSystemUtils.formatPath(fileObject.getFileFullName());
        if (!path0.startsWith("/")) {
            path0 = "/" + path0;
        }
        DynamicFile file = DynamicFile.file(setting.workHome + "/files" + path0);
        final String fileKey = FileSystemUtils.formatPath(path0 + File.separator + fileObject.getFileFullName());
        if (fileObject.getFileFingerprint().equals(oldFileFingerprint) && file.exists()) {
            byte[] oldData = FileUtils.readFileToByteArray(file.getFile().getFile());
            String oldFileFingerprint1 = DigestUtils.md5Hex(oldData);
            if (oldFileFingerprint1.equals(fileObject.getFileFingerprint())) {
                return;
            } else {
                //指纹不一样，可能存在人为修改
            }
        }
        FileTransaction transaction = file.begin();
        try {
            File tempFile = transaction.getFile();
            //实现HTTP的下载方式
            String downloadUrl = fileObject.getTransferType().getCode() + "://" + fileObject.getHost() + ":" + fileObject.getPort()
                    + "/file/download?"
                    + "&groupId=" + setting.groupId
                    + "&artifactId=" + setting.artifactId
                    + "&version=" + setting.version
                    + "&env=" + setting.env
                    + "&machine=" + setting.machine
                    + "&fileFullName=" + FileSystemUtils.formatPath(fileObject.getFileFullName());
            log.info("download url: {}", downloadUrl);

            Http http = Http.post(downloadUrl)
                    .acceptCharset("iso-8859-1")
                    .connectTimeout(6000)
                    .readTimeout(5000)
                    .useCaches(false)//不允许缓存
                    .contentType("application/octet-stream", "iso-8859-1");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            http.receive(buffer);
            byte[] newData = null;
            if (fileObject.isEncrypt()) {
                newData = AES.decrypt(setting.getSecurityKey(), AES.DEFAULT_IV, buffer.toByteArray());
            } else {
                newData = buffer.toByteArray();
            }
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(newData);
            fos.flush();
            fos.close();

            if (setting.printLog) {
                log.info("download file '{}'", fileObject.getFileFullName());
            }
            String fileFingerprint = DigestUtils.md5Hex(newData);
            //检查新的指纹和下载文件指纹是否一致，否则可能损坏
            if (fileFingerprint.equals(fileObject.getFileFingerprint())) {
                if (setting.printLog) {
                    log.info("commit file '{}' fingerprint:{} is ok!", fileObject.getFileFullName(), fileFingerprint);
                }
                transaction.commit();
                //如果存在分发路径,则使用线程池执行，避免主线程阻塞
                if (StringUtils.isNotBlank(fileObject.getDistPath())) {
                    final File newFile = transaction.getFile();
                    final String copyFilePath = fileObject.getDistPath();
                    final String filePath = fileKey;
                    final boolean printLog0 = setting.printLog;
                    fileCopyPool.submit(new Runnable() {
                        public void run() {
                            //如果存在文件
                            try {
                                File targetFile = new File(copyFilePath);
                                if (targetFile.exists()) {
                                    FileUtils.forceDelete(targetFile);

                                }
                                FileUtils.copyFile(newFile, targetFile);
                                if (printLog0) {
                                    log.info("copy file '{}' to '{}' success!", filePath, copyFilePath);
                                }
                            } catch (IOException e) {
                                log.error("force delete file " + copyFilePath + " happens error!", e);
                            }
                        }
                    });

                }
            } else {
                if (setting.printLog) {
                    log.error("error file '{}' local fingerprint:{}, remote fingerprint:{}!", fileObject.getFileFullName(), fileFingerprint, fileObject.getFileFingerprint());
                }
                transaction.rollback();
            }
        } catch (IOException e) {
            log.error(MessageFormatter.format("rollback file '{}' , happens error!", fileObject.getFileFullName()), e);
            transaction.rollback();
        }
    }

    public InputStream openFile(String fileName) throws IOException {
        if (fileName == null) {
            throw ErrorContextFactory.instance().message("fileName is ").runtimeException();
        }
        String fileName0 = FileSystemUtils.formatPath(fileName);
        int lastDotIdx = fileName0.lastIndexOf("/");
        String filePath = fileName0.substring(0, lastDotIdx);
        fileName = fileName0.substring(lastDotIdx + 1);
        return openFile(filePath, fileName);
    }

    /**
     * 打开一个文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 输入流
     */
    public InputStream openFile(String filePath, String fileName) throws IOException {
        filePath = FileSystemUtils.formatPath(filePath);
        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }
        FileObject newFileObject = CONFIG_CACHE.getFile(filePath + "/" + fileName);
        if (newFileObject == null) {
            throw new FileNotFoundException(MessageFormatter.format("'{}' not found !", filePath + "/" + fileName));
        }
        //从动态文件读取缓存文件
        DynamicFile file = DynamicFile.file(new File(setting.workHome + "/files" + filePath), fileName);
        //懒下载
        if (newFileObject.isLazyDownload()) {
            //下载
            if (newFileObject.getTransferType() == FileTransferType.HTTP) {
                //实现HTTP的下载方式
                try {
                    downloadFile(newFileObject, null);
                } catch (Exception e) {
                    log.error("http download file " + filePath + "/" + fileName + " happens error!", e);
                }
            } else {
                log.error("'{}'not support download transfer protocol '{}'", filePath + "/" + fileName, newFileObject.getTransferType());
            }
        }

        if (!file.exists()) {
            throw new FileNotFoundException(MessageFormatter.format("'{}' not found!", filePath + "/" + fileName));
        }
        return file.getFile().stream();
    }



    /**
     * 获取本地的信息
     *
     * @return
     */
    public String getRuntime() {
        //写入请求的参数
        String json = GSON.toJson(CONFIG_CACHE);
        return json;
    }

    public Properties getProperties() {
        return this.runtimeProperties;
    }

    /**
     * 获取一个参数
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @param type         数据类型
     * @param <T>
     * @return 数据
     */
    public <T> T getProperty(String name, String defaultValue, Class<T> type) {
        String value = runtimeProperties.getProperty(name, defaultValue);
        return ValueUtils.convert(value, type);
    }

}
