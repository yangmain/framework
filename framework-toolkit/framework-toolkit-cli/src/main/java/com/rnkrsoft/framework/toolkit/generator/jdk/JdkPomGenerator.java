package com.rnkrsoft.framework.toolkit.generator.jdk;

import com.devops4j.io.buffer.ByteBuf;
import com.devops4j.message.MessageFormatter;
import com.rnkrsoft.framework.toolkit.generator.GenerateContext;
import com.rnkrsoft.framework.toolkit.generator.PomGenerator;
import com.rnkrsoft.framework.toolkit.generator.Scope;

/**
 * Created by rnkrsoft.com on 2018/5/6.
 */
public class JdkPomGenerator extends JdkGenerator implements PomGenerator {
    @Override
    public ByteBuf generate(GenerateContext ctx) {
        ByteBuf buf = ByteBuf.allocate(1024).autoExpand(true);
        buf.put("UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "\n");
        buf.put("UTF-8", "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"", "\n");
        buf.put("UTF-8", indent(), "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "\n");
        buf.put("UTF-8", indent(), "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">", "\n");
        buf.put("UTF-8", indent(), "<modelVersion>4.0.0</modelVersion>", "\n");
        buf.put("UTF-8", indent(), "<parent>", "\n");
        buf.put("UTF-8", indent(2), MessageFormatter.format("<groupId>{}</groupId>", ctx.getPackageName()), "\n");
        buf.put("UTF-8", indent(2), "<artifactId>xxx-dao</artifactId>", "\n");
        buf.put("UTF-8", indent(2), "<version>1.0.0-SNAPSHOT</version>", "\n");
        buf.put("UTF-8", indent(), "</parent>", "\n");
        buf.put("UTF-8", indent(), "<artifactId>xxx-xxx-dao</artifactId>", "\n");
        buf.put("UTF-8", indent(), "<dependencies>", "\n");
        dependency(buf, "mysql", "mysql-connector-java", Scope.compile);
        dependency(buf, "com.rnkrsoft.framework", "framework-standalone", Scope.compile);
        dependency(buf, "com.rnkrsoft.framework", "framework-test-standalone", Scope.compile);
        dependency(buf, "org.mybatis", "mybatis", Scope.compile);
        dependency(buf, "commons-dbcp", "commons-dbcp", Scope.test);
        dependency(buf, "org.springframework", "spring-core", Scope.compile);
        dependency(buf, "org.springframework", "spring-beans", Scope.compile);
        dependency(buf, "org.springframework", "spring-context", Scope.compile);
        dependency(buf, "org.springframework", "spring-context-support", Scope.compile);
        dependency(buf, "org.springframework", "spring-aop", Scope.compile);
        dependency(buf, "org.springframework", "spring-tx", Scope.compile);
        dependency(buf, "org.springframework", "spring-jdbc", Scope.compile);
        dependency(buf, "org.springframework", "spring-test", Scope.test);
        buf.put("UTF-8", indent(), "</dependencies>", "\n");
        buf.put("UTF-8", "</project>", "\n");
        return buf;
    }

    /**
     * 生成依赖
     *
     * @param buf
     * @param groupId
     * @param artifactId
     */
    void dependency(ByteBuf buf, String groupId, String artifactId, Scope scope) {
        buf.put("UTF-8", indent(2), "<dependency>", "\n");
        buf.put("UTF-8", indent(3), MessageFormatter.format("<groupId>{}</groupId>", groupId), "\n");
        buf.put("UTF-8", indent(3), MessageFormatter.format("<artifactId>{}</artifactId>", artifactId), "\n");
        buf.put("UTF-8", indent(3), MessageFormatter.format("<scope>{}</scope>", scope.name()), "\n");
        buf.put("UTF-8", indent(2), "</dependency>", "\n");
    }
}
