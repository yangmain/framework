package com.rnkrsoft.framework.config.v1;

import lombok.*;

import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 文件对象
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileObject {
    /**
     * 文件所在服务器地址
     */
    String host = "127.0.0.1";
    /**
     * 文件所在服务器端口
     */
    int port = 80;
    /**
     * 文件名称
     */
    String fileName;

    /**
     * 文件路径
     */
    String filePath;
    /**
     * 描述
     */
    String desc;
    /**
     * 目标路径
     */
    String distPath;

    /**
     * 是否启用
     */
    boolean enabled = false;
    /**
     * 懒下载
     */
    boolean lazyDownload;
    /**
     * 是否加密
     */
    boolean encrypt = false;
    /**
     * 传输类型
     */
    int transferType = FileTransferType.HTTP.code;

    /**
     * 文件指纹
     */
    String fileFingerprint;

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
    Date createTime;

    /**
     * 修改时间
     */
    Date updateTime;

    public void setTransferType(FileTransferType fileTransferType){
        this.transferType = fileTransferType.code;
    }

    public FileTransferType getTransferType(){
        return FileTransferType.valueOfCode(this.transferType);
    }
}