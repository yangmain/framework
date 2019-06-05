package com.rnkrsoft.framework.config.v1;

import lombok.*;

import javax.web.doc.AbstractResponse;
import javax.web.doc.annotation.ApidocElement;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushResponse extends AbstractResponse{
    @ApidocElement("参数总数")
    int totalParamCount;
    @ApidocElement("新建参数个数")
    int newParamCount;
    @ApidocElement("修改参数个数")
    int updateParamCount;
    @ApidocElement("文件总数")
    int totalFileCount;
    @ApidocElement("新建文件个数")
    int newFileCount;
    @ApidocElement("修改文件个数")
    int updateFileCount;
}