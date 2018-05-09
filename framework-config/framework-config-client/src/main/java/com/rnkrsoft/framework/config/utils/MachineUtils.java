package com.rnkrsoft.framework.config.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
public class MachineUtils {
    /**
     * 获取当前机器HOSTNAME
     * @return
     */
    public static String getHostName() {
        try {
            return (InetAddress.getLocalHost()).getCanonicalHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }
}
