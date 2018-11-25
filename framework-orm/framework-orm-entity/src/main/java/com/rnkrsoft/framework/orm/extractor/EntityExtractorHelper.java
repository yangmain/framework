package com.rnkrsoft.framework.orm.extractor;

import com.rnkrsoft.framework.orm.EntityExtractor;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Reflector;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.jdbc.Table;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体信息提取器
 */
@Slf4j
public final class EntityExtractorHelper {

    EntityExtractor extractor;

    public static final Map<Class, TableMetadata> TABLES_CACHE = new HashMap<Class, TableMetadata>();

    /**
     * 提取实体上的元信息
     *
     * @param entityClass 实体类
     * @param strict      严格使用ORM注解
     * @return 元信息
     */
    public TableMetadata extractTable(Class entityClass, boolean strict) {
        //如果缓存包含则直接返回
        if (TABLES_CACHE.containsKey(entityClass)) {
            return TABLES_CACHE.get(entityClass);
        }
        javax.persistence.Table tableAnnJPA = (javax.persistence.Table) entityClass.getAnnotation(javax.persistence.Table.class);
        Table tableAnnORM = (Table) entityClass.getAnnotation(Table.class);
        if (tableAnnJPA == null && tableAnnORM == null) {
            throw ErrorContextFactory.instance().activity("提取实体类{}的元信息", entityClass)
                    .message("没有使用JPA注解{}，也没使用ORM注解{}", javax.persistence.Table.class, Table.class)
                    .solution("建议使用{}注解", Table.class)
                    .runtimeException();
        }
        if (tableAnnJPA != null && tableAnnORM != null) {
            throw ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("同时使用JPA注解{}和使用ORM注解{}", javax.persistence.Table.class, Table.class)
                    .solution("建议使用{}注解，也可以使用JPA注解{}", Table.class, javax.persistence.Table.class)
                    .runtimeException();
        }
        TableMetadata tableMetadata = TableMetadata.builder().entityClass(entityClass).entityClassName(entityClass.getSimpleName()).build();
        //严格模式下，只能使用Wing4j注解
        if (strict) {
            if (tableAnnORM == null) {
                throw ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息", entityClass)
                        .message("由于已开启强制使用ORM注解，但是实际使用ORM注解{}", javax.persistence.Table.class)
                        .solution("必须强制使用ORM注解", Table.class)
                        .runtimeException();
            }
            checkCouldNotUseJpaAnnotation(tableMetadata);
        }

        //解析JPA注解
        if (tableAnnJPA != null) {
            extractor = new JpaEntityExtractor();
        } else if (tableAnnORM != null) {
            extractor = new OrmEntityExtractor();
        }
        extractor.extractTable(tableMetadata);
        //提取字段
        extractFields(tableMetadata);
        if (tableMetadata.getPrimaryKeys().isEmpty()) {
            throw ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("不允许无物理主键的实体")
                    .solution("建议在主键字段标注{}注解，也可以使用JPA注解{}", PrimaryKey.class, javax.persistence.Id.class)
                    .runtimeException();
        }
        return tableMetadata;
    }

    /**
     * 检查不能用JPA注解
     *
     * @param tableMetadata 表元信息
     */
    public void checkCouldNotUseJpaAnnotation(TableMetadata tableMetadata) {
        Reflector reflector = GlobalSystemMetadata.reflector(tableMetadata.getEntityClass());
        for (Field field : reflector.getFields()) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class annClazz = annotation.annotationType();
                boolean useJpa = annClazz.getName().startsWith("javax.persistence.");
                if (useJpa) {
                    throw ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", tableMetadata.getEntityClass())
                            .message("已开启严格ORM模式，禁止使用JPA注解，当前使用了{}注解", annClazz.getName())
                            .solution("将所有字段标注JPA注解转换成等价的ORM注解")
                            .runtimeException();
                }
            }
        }
    }

    /**
     * 提取实体类的所有字段
     *
     * @param tableMetadata 表元信息
     */
    public void extractFields(TableMetadata tableMetadata) {
        Reflector reflector = GlobalSystemMetadata.reflector(tableMetadata.getEntityClass());
        for (Field field : reflector.getFields()) {
            //任意使用了一个字段注解的
            ColumnMetadata columnMetadata = ColumnMetadata.builder()
                    .tableMetadata(tableMetadata)
                    .entityClass(tableMetadata.getEntityClass())
                    .columnField(field)
                    .javaName(field.getName())
                    .javaType(field.getType())
                    .build();
            //提取该字段元信息
            if (!extractor.extractField(columnMetadata)) {
                continue;
            }
            KeywordsUtils.validateKeyword(columnMetadata);
            //保存有序的字段
            tableMetadata.getOrderColumns().add(columnMetadata.getJdbcName());
            tableMetadata.getColumnMetadataSet().put(columnMetadata.getJdbcName(), columnMetadata);
        }
    }
}
