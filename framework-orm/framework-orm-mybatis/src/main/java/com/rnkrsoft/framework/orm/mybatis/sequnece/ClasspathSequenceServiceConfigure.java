package com.rnkrsoft.framework.orm.mybatis.sequnece;

import com.rnkrsoft.framework.sequence.SequenceService;
import lombok.Setter;

import javax.activation.DataSource;
import java.lang.reflect.Method;

/**
 * Created by devops4j on 2017/1/8.
 */
public class ClasspathSequenceServiceConfigure extends AbstractSequenceServiceConfigure {
    @Setter
    DataSource dataSource;
    @Override
    public SequenceService init(String tableName, Class clazz) {
        try {
            SequenceService instance = (SequenceService) clazz.newInstance();
            //设置数据源方法
            Method setDataSourceMethod = null;
            try {
                setDataSourceMethod = clazz.getMethod("setDataSource", new Class[]{DataSource.class});
                setDataSourceMethod.invoke(instance, dataSource);
            } catch (NoSuchMethodException e) {

            }
        } catch (Exception e) {
        }
        return null;
    }
}
