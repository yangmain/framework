package com.rnkrsoft.framework.monitor.heartbeat.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
@Slf4j
@Controller
@RequestMapping("/monitor")
public class HeartbeatController {
    @Autowired
    @Qualifier("defaultDataSource")
    DataSource dataSource;

    @RequestMapping("/heartbeat")
    public Object heartbeat() throws Exception {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute("SELECT 'x'");
            return "success";
        } catch (Exception e) {
            log.error("访问数据库失败", e);
            throw e;
        }
    }
}
