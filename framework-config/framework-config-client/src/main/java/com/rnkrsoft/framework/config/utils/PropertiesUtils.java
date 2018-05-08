package com.rnkrsoft.framework.config.utils;

import java.util.Properties;

public class PropertiesUtils {

    public static Properties clone(Properties props) {
        Properties properties = new Properties();
        if (props == null) {
            return properties;
        }
        for (String key : props.stringPropertyNames()) {
            properties.setProperty(key, props.getProperty(key));
        }
        return properties;
    }
}