package com.rnkrsoft.framework.config.v1;

import lombok.Data;
import lombok.ToString;

import javax.web.doc.annotation.ApidocElement;
import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
@Data
@ToString
public class InstructRequest implements Serializable{
    @ApidocElement("指令类型")
    InstructionType type = InstructionType.FETCH;
    @ApidocElement("指令数据")
    String data;
}
