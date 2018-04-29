package com.rnkrsoft.framework.toolkit;

import com.devops4j.io.buffer.ByteBuf;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.toolkit.Command;
import com.rnkrsoft.framework.toolkit.CommandCollection;
import com.rnkrsoft.framework.toolkit.CommandDefine;
import com.rnkrsoft.framework.toolkit.HelpFormatter;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
import com.rnkrsoft.framework.toolkit.generator.Generator;
import com.rnkrsoft.framework.toolkit.generator.jdk.JdkDaoGenerator;
import com.rnkrsoft.framework.toolkit.generator.jdk.JdkEntityGenerator;
import com.rnkrsoft.framework.toolkit.jdbc.JdbcReverse;
import com.rnkrsoft.framework.toolkit.jdbc.JdbcReverseMySQL;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2017/1/2.
 */
@Slf4j
public class Main {
    protected int commandCount = 0;
    /**
     * 存放执行过的历史命令
     */
    protected HashMap<Integer, String> history = new HashMap<Integer, String>();
    /**
     * 命令集合
     */
    protected static CommandCollection COMMAND_COLLECTION = new CommandCollection();

    static {
        {
            CommandDefine define = new CommandDefine();
            define.addOption("url", "h", true, 1, "数据库地址", "192.168.1.106:3306");
            define.addOption("username", "u", true, 1, "用户名", "root");
            define.addOption("password", "p", true, 1, "密码", "root");
            define.addOption("schema", "s", true, 1, "数据库模式", "rnkrsoft");
            define.addOption("package", "f", true, 1, "保存包名", "com.rnkrsoft.framework");
            define.setName("逆向工程");
            define.setCmd("reverse");
            define.setExample("reverse -h 192.168.1.106:3306 -u root -p root -schema rnkrsoft -package com.rnkrsoft.framework");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("帮助信息");
            define.setCmd("help");
            define.addOption("cmd", "c", false, 1, "命令", "reverse");
            define.setExample("help -c reverse");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("exit");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/help");
            COMMAND_COLLECTION.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("查看历史命令");
            define.setCmd("history");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.rnkrsoft.com/help");
            COMMAND_COLLECTION.addDefine(define);
        }
    }

    /**
     * 保存执行过的历史
     *
     * @param i   序号
     * @param cmd 命令
     */
    protected void addToHistory(int i, String cmd) {
        history.put(i, cmd);
    }

    protected String getPrompt() {
        return "cli>";
    }

    /**
     * 用法提示方法
     */
    static void usage() {
        System.out.println("toolkit用法:");
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.render("", COMMAND_COLLECTION, "");
    }

    protected boolean processCmd(Command command) {
        String cmd = command.getCmd();
        if (cmd.equals("exit")) {
            System.out.println("正在退出...");
            System.exit(0);
        } else if (cmd.equals("history")) {
            System.out.println("历史命令");
            for (int i = commandCount - 10; i <= commandCount; ++i) {
                if (i < 0) continue;
                System.out.println(i + " - " + history.get(i));
            }
        } else if (cmd.equals("reverse")) {
            String packageName = command.valueString("package");
            String schema = command.valueString("schema");
            String h = command.valueString("url");
            String u = command.valueString("username");
            String p = command.valueString("password");
            if (packageName == null) {
                System.out.println("package is empty!");
                return false;
            }
            if (schema == null) {
                System.out.println("schema is empty!");
                return false;
            }

            try {
                JdbcReverse jdbcReverse = new JdbcReverseMySQL();
                List<TableMetadata> metadatas = jdbcReverse.reverses(h, schema, u, p, packageName);
                Generator entityGenerator = new JdkEntityGenerator();
                Generator daoGenerator = new JdkDaoGenerator();
                for (TableMetadata metadata : metadatas) {
                    GenerateContext ctx = GenerateContext.builder().packageName(packageName).tableMetadata(metadata).build();
                    ByteBuf entityCode = entityGenerator.generate(ctx);
                    ByteBuf daoCode = daoGenerator.generate(ctx);
                    File entityFile = new File(metadata.getEntityClassName().replaceAll("\\.", "/") + ".java");
                    File daoFile = new File(metadata.getDaoClassName().replaceAll("\\.", "/") + ".java");
                    entityFile.getParentFile().mkdirs();
                    daoFile.getParentFile().mkdirs();
                    FileOutputStream entityFileFos = new FileOutputStream(entityFile);
                    FileOutputStream daoFileFos = new FileOutputStream(daoFile);
                    try{
                        entityCode.write(entityFileFos);
                        daoCode.write(daoFileFos);
                    }finally {
                        IOUtils.closeQuietly(entityFileFos);
                        IOUtils.closeQuietly(daoFileFos);
                    }
                }
            } catch (Exception e) {
                log.error("逆向生成失败", e);
            }
        } else if (cmd.equals("clean")) {
            for (int i = 0; i < 30; i++) {
                System.out.println("\n");
            }
        } else if (cmd.equals("help")) {
            if (command.getArgs().isEmpty()) {
                usage();
            } else if (command.hasOption("cmd")) {
                String arg = command.valueString("cmd");
                CommandDefine define = COMMAND_COLLECTION.getOptionCollection().get(arg);
                HelpFormatter formatter = new HelpFormatter();
                formatter.render("", define, "");
            }
        }
        return true;
    }

    /**
     * 解析命令行
     *
     * @param line 命令字符串
     */
    public void executeLine(String line) {
        if (!line.equals("")) {
            String[] args = line.split(" ");
            CommandDefine define = COMMAND_COLLECTION.getOptionCollection().get(args[0]);
            if (define == null) {
                System.out.println(COMMAND_COLLECTION.getOptionCollection().keySet());
                System.out.println("无效的命令【" + args[0] + "】");
                return;
            }
            Command command = define.parseCommand(line);
            addToHistory(commandCount, line);
            processCmd(command);
            commandCount++;
        }
    }

    void run() throws IOException {
        System.out.println("Welcome to rnkrsoft orm toolkit!");
        boolean jlinemissing = false;
        // only use jline if it's in the classpath
        try {
            Class consoleC = Class.forName("jline.console.ConsoleReader");
            Object console = consoleC.newInstance();
            System.out.println("JLine support is enabled");
            Method addCompleter = consoleC.getMethod("addCompleter", Completer.class);
            addCompleter.invoke(console, new FileNameCompleter());
            String line;
            Method readLine = consoleC.getMethod("readLine", String.class);
            while ((line = (String) readLine.invoke(console, getPrompt())) != null) {
                executeLine(line);
            }
        } catch (Exception e) {
            log.debug("Unable to start jline", e);
            jlinemissing = true;
        }

        if (jlinemissing) {
            System.out.println("JLine support is disabled");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = br.readLine()) != null) {
                executeLine(line);
            }
        }

    }

    /**
     * 启动CLI命令
     *
     * @param args 参数
     * @throws IOException 异常
     */
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }
}
