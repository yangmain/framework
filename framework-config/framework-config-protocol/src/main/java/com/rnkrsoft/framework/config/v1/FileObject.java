package com.rnkrsoft.framework.config.v1;

import lombok.*;

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
     * 文件唯一编号
     */
    String fileId;
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
    String fileFullName;
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
    String createTime;

    /**
     * 修改时间
     */
    String updateTime;

    public void setTransferType(FileTransferType fileTransferType){
        this.transferType = fileTransferType.code;
    }

    public FileTransferType getTransferType(){
        return FileTransferType.valueOfCode(this.transferType);
    }
}
