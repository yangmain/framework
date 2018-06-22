package com.rnkrsoft.framework.sequence;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * 序号服务工厂类
 */
@Slf4j
public class SequenceServiceFactory {
    /**
     * 返回最先匹配的序号服务
     *
     * @return 序号服务
     */
    public static SequenceService instance() {
        return instance(null);
    }

    /**
     * 返回指定实现类的序号服务
     *
     * @param className 实现类
     * @return 序号服务
     */
    public static SequenceService instance(String className) {
        SequenceService service = null;
        ServiceLoader<SequenceService> serviceLoader = ServiceLoader.load(SequenceService.class);
        Iterator<SequenceService> serviceIterator = serviceLoader.iterator();
        while (serviceIterator.hasNext()) {
            SequenceService service0 = serviceIterator.next();
            //如果没有指定实现类，发现则赋值
            if (className == null) {
                service = service0;
                return service;
            } else {//如果指实现类，只有匹配实现类才赋值
                if (service0.getClass().getName().equals(className)) {
                    service = service0;
                    return service;
                }
            }
        }
        if (service == null) {
            serviceIterator = serviceLoader.iterator();
            while (serviceIterator.hasNext()) {
                SequenceService service0 = serviceIterator.next();
                log.info("implement '{}'", service0.getClass());
            }
            throw ErrorContextFactory.instance()
                    .activity("scan SequenceService")
                    .message("not found implement class '{}'", className)
                    .solution("please check configuration implement class jar package dependency or spelling error which implement class name!")
                    .runtimeException();
        }
        return service;
    }
}
