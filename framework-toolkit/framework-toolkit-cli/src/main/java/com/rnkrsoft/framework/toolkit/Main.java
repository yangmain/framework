//package com.devops4j.framework4j.toolkit.cli;
//
//import jline.console.completer.Completer;
//import jline.console.completer.FileNameCompleter;
//import lombok.extern.slf4j.Slf4j;
//import com.devops4j.framework4j.common.MessageFormatter;
//import com.devops4j.framework4j.orm.codegen.DaoCodeGen;
//import com.devops4j.framework4j.orm.codegen.EntityCodeGen;
//import com.devops4j.framework4j.orm.entity.metadata.TableMetadata;
//import com.devops4j.framework4j.orm.entity.utils.ReverseEntityUtils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by devops4j on 2017/1/2.
// */
//@Slf4j
//public class Main {
//    protected int commandCount = 0;
//    /**
//     * 存放执行过的历史命令
//     */
//    protected HashMap<Integer, String> history = new HashMap<Integer, String>();
//    /**
//     * 命令集合
//     */
//    protected static CommandCollection commandCollection = new CommandCollection();
//
//    static {
//        {
//            CommandDefine define = new CommandDefine();
//            define.addOption("url", "h", true, 1, "数据库地址", "jdbc:mysql://192.168.1.106:3306/devops4j");
//            define.addOption("username", "u", true, 1, "用户名", "root");
//            define.addOption("password", "p", true, 1, "密码", "root");
//            define.addOption("schema", "s", true, 1, "数据库模式", "devops4j");
//            define.addOption("package", "f", true, 1, "保存包名", "com.devops4j.framework4j.entity");
//            define.setName("逆向工程");
//            define.setCmd("reverse");
//            define.setExample("reverse -h jdbc:mysql://192.168.1.106:3306/devops4j -u root -p root -schema devops4j -package com.devops4j.framework4j.demo");
//            define.setExtrInfo("有关详细信息, 请参阅 http://www.devops4j.com/help");
//            commandCollection.addDefine(define);
//        }
//        {
//            CommandDefine define = new CommandDefine();
//            define.setName("帮助信息");
//            define.setCmd("help");
//            define.addOption("cmd", "c", false, 1, "命令", "reverse");
//            define.setExample("help -c reverse");
//            define.setExtrInfo("有关详细信息, 请参阅 http://www.devops4j.com/help");
//            commandCollection.addDefine(define);
//        }
//        {
//            CommandDefine define = new CommandDefine();
//            define.setName("退出CLI");
//            define.setCmd("exit");
//            define.setExtrInfo("有关详细信息, 请参阅 http://www.devops4j.com/help");
//            commandCollection.addDefine(define);
//        }
//        {
//            CommandDefine define = new CommandDefine();
//            define.setName("查看历史命令");
//            define.setCmd("history");
//            define.setExtrInfo("有关详细信息, 请参阅 http://www.devops4j.com/help");
//            commandCollection.addDefine(define);
//        }
//    }
//
//    /**
//     * 保存执行过的历史
//     *
//     * @param i   序号
//     * @param cmd 命令
//     */
//    protected void addToHistory(int i, String cmd) {
//        history.put(i, cmd);
//    }
//
//    protected String getPrompt() {
//        return "cli>";
//    }
//
//    /**
//     * 用法提示方法
//     */
//    static void usage() {
//        System.out.println("Wing4j toolkit用法:");
//        HelpFormatter formmatter = new HelpFormatter();
//        formmatter.render("", commandCollection, "");
//    }
//
//    protected boolean processCmd(Command command) {
//        String cmd = command.getCmd();
//        if (cmd.equals("exit")) {
//            System.out.println("正在退出...");
//            System.exit(0);
//        } else if (cmd.equals("history")) {
//            System.out.println("历史命令");
//            for (int i = commandCount - 10; i <= commandCount; ++i) {
//                if (i < 0) continue;
//                System.out.println(i + " - " + history.get(i));
//            }
//        } else if (cmd.equals("reverse")) {
//            String packageName = command.valueString("package");
//            String schema = command.valueString("schema");
//            String h = command.valueString("url");
//            String u = command.valueString("username");
//            String p = command.valueString("password");
//            if (packageName == null) {
//                System.out.println("package is empty!");
//                return false;
//            }
//            if (schema == null) {
//                System.out.println("schema is empty!");
//                return false;
//            }
//            String headFormat = "/**\n" +
//                    " * {} \n" +
//                    " */";
//            String head = MessageFormatter.format(headFormat, "the file is using devops4j-toolkit auto codegen!");
//            try {
//                String entityPackageName = packageName + ".entity";
//                String daoPackageName = packageName + ".dao";
//                List<TableMetadata> tableMetadatas = ReverseEntityUtils.reverseFormDatabase(schema, h, u, p);
//                EntityCodeGen.generate(head, null, tableMetadatas, entityPackageName, ".");
//                DaoCodeGen.generate(head, null, tableMetadatas, daoPackageName, entityPackageName, ".");
//            } catch (Exception e) {
//                log.error("逆向生成失败", e);
//            }
//        } else if (cmd.equals("clean")) {
//            for (int i = 0; i < 30; i++) {
//                System.out.println("\n");
//            }
//        } else if (cmd.equals("help")) {
//            if (command.getArgs().isEmpty()) {
//                usage();
//            }else if(command.hasOption("cmd")){
//                String arg = command.valueString("cmd");
//                CommandDefine define = commandCollection.getOptionCollection().get(arg);
//                HelpFormatter formatter = new HelpFormatter();
//                formatter.render("", define, "");
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 解析命令行
//     *
//     * @param line 命令字符串
//     */
//    public void executeLine(String line) {
//        if (!line.equals("")) {
//            String[] args = line.split(" ");
//            CommandDefine define = commandCollection.getOptionCollection().get(args[0]);
//            if (define == null) {
//                System.out.println(commandCollection.getOptionCollection().keySet());
//                System.out.println("无效的命令【" + args[0] + "】");
//                return;
//            }
//            Command command = define.parseCommand(line);
//            addToHistory(commandCount, line);
//            processCmd(command);
//            commandCount++;
//        }
//    }
//
//    void run() throws IOException {
//        System.out.println("Welcome to Wing4j toolkit!");
//        boolean jlinemissing = false;
//        // only use jline if it's in the classpath
//        try {
//            Class consoleC = Class.forName("jline.console.ConsoleReader");
//            Object console = consoleC.newInstance();
//            System.out.println("JLine support is enabled");
//            Method addCompleter = consoleC.getMethod("addCompleter", Completer.class);
//            addCompleter.invoke(console, new FileNameCompleter());
//            String line;
//            Method readLine = consoleC.getMethod("readLine", String.class);
//            while ((line = (String) readLine.invoke(console, getPrompt())) != null) {
//                executeLine(line);
//            }
//        } catch (Exception e) {
//            log.debug("Unable to start jline", e);
//            jlinemissing = true;
//        }
//
//        if (jlinemissing) {
//            System.out.println("JLine support is disabled");
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            String line;
//            while ((line = br.readLine()) != null) {
//                executeLine(line);
//            }
//        }
//
//    }
//
//    /**
//     * 启动CLI命令
//     *
//     * @param args 参数
//     * @throws IOException 异常
//     */
//    public static void main(String[] args) throws IOException {
//        Main main = new Main();
//        main.run();
//    }
//}
