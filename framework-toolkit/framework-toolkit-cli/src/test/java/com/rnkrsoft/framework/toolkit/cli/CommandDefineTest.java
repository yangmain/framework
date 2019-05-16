package com.rnkrsoft.framework.toolkit.cli;

import org.junit.Test;

/**
 * Created by Administrator on 2018/6/25.
 */
public class CommandDefineTest {

    @Test
    public void testParseOptions() throws Exception {
        CommandCollection COMMAND_COLLECTION = new CommandCollection();
        {
            CommandDefine define = new CommandDefine();
            define.addOption("url", "h", true, 1, "数据库地址", "127.0.0.1:3306", "");
            define.addOption("username", "u", false, 1, "用户名", "字符串用户名", "root");
            define.addOption("password", "p", false, 1, "密码", "字符串密码", "");
            define.addOption("schema", "s", true, 1, "数据库模式", "字符串数据库名", "");
            define.addOption("package", "f", false, 1, "保存包名", "java格式的包名", "com.rnkrsoft");
            define.addOption("output", "o", false, 1, "输出路径", ".", ".");
            define.addOption("prefixes", "pres", false, -1, "前缀", "表名前缀", "tb");
            define.addOption("suffixes", "sufs", false, -1, "后缀", "表名后缀", "");
            define.setName("逆向工程");
            define.setCmd("reverse");
            define.setExample("reverse -h mysql.rnkrsoft.com:3306 -u root -p root -schema demo -package com.rnkrsoft.framework");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("帮助信息");
            define.setCmd("help");
            define.addOption("cmd", "c", false, 1, "命令", "reverse");
            define.setExample("help -c reverse");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("exit");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("查看历史命令");
            define.setCmd("history");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        String cmd = "reverse -h 192.168.0.111:3333 -u root -p root -schema demo -pres tb srv";
        Command command = COMMAND_COLLECTION.getOptionCollection().get("reverse").parseCommand(cmd);
        System.out.println(command);
    }
}