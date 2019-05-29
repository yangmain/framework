package com.rnkrsoft.framework.config.v1;

import lombok.Data;

import javax.web.doc.AbstractResponse;
import javax.web.doc.annotation.ApidocElement;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
@Data
public class InstructResponse extends AbstractResponse{
    @ApidocElement("指令类型")
    InstructionType type = InstructionType.FETCH;
    @ApidocElement("指令数据")
    String data;
}
