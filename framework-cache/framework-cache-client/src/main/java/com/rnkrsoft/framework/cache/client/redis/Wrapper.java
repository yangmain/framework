package com.rnkrsoft.framework.cache.client.redis;

import lombok.Data;

import java.io.Serializable;
@Data
public class Wrapper implements Serializable {
    long serialVersionUID = 1024L;
    String className;
    String data;
}