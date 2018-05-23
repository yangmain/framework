package com.rnkrsoft.framework.toolkit.cli;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 命令对象抽象封装
 */
public class Command {
    /**
     * 执行命令的参数名
     */
    @Getter
    protected final Map<String, List<String>> args = new HashMap<String, List<String>>();
    /**
     * 命令
     */
    @Getter
    protected String cmd = null;

    @Getter
    protected CommandDefine commandDefine = null;

    public Command(String cmd, CommandDefine commandDefine) {
        this.cmd = cmd;
        this.commandDefine = commandDefine;
    }

    /**
     * 检查是否有传入指定的参数
     *
     * @param name 参数名字
     * @return 含有返回真，反之为假
     */
    public boolean hasOption(String name) {
        if (args.containsKey(name)) {
            return true;
        } else {
            String longName = commandDefine.alias.get(name);
            return args.containsKey(longName);
        }
    }

    /**
     * 提取参数数组
     *
     * @param name 参数名字
     * @return 参数值
     */
    public String[] valueArray(String name) {
        if (args.containsKey(name)) {
            List<String> values = args.get(name);
            return values.toArray(new String[values.size()]);
        }
        String longName = commandDefine.alias.get(name);
        if (args.containsKey(longName)) {
            List<String> values = args.get(longName);
            return values.toArray(new String[values.size()]);
        } else {
            return null;
        }
    }

    /**
     * 获取参数值
     *
     * @param name 参数名字
     * @return 参数值
     */
    public String valueString(String name) {
        if (args.containsKey(name)) {
            List<String> args0 = args.get(name);
            return args0 == null ? null : args0.get(0);
        }
        String longName = commandDefine.alias.get(name);
        if (args.containsKey(longName)) {
            List<String> args0 = args.get(longName);
            return args0 == null ? null : args0.get(0);
        } else {
            return null;
        }
    }
}
