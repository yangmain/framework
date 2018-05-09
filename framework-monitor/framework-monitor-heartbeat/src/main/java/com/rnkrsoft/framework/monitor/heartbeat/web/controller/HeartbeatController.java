package com.rnkrsoft.framework.monitor.heartbeat.web.controller;

import com.rnkrsoft.framework.monitor.heartbeat.dao.HeartbeatDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
@Slf4j
@Controller
@RequestMapping("/monitor")
public class HeartbeatController {
    @Autowired
    HeartbeatDAO heartbeatDAO;

    @RequestMapping("/heartbeat")
    public Object heartbeat() throws Exception {
        try {
            heartbeatDAO.countAll();
            return "success";
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}
