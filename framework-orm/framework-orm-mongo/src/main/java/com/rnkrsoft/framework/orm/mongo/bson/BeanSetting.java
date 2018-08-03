package com.rnkrsoft.framework.orm.mongo.bson;

import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.sequence.spring.SequenceServiceConfigure;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BeanSetting {
    /**
     * 是否允许null值
     */
    boolean nullable;
    TableMetadata tableMetadata;
    SequenceServiceConfigure sequenceServiceConfigure;

    public BeanSetting() {
        this.nullable = true;
    }

    public BeanSetting(boolean nullable, TableMetadata tableMetadata, SequenceServiceConfigure sequenceServiceConfigure) {
        this.nullable = nullable;
        this.tableMetadata = tableMetadata;
        this.sequenceServiceConfigure = sequenceServiceConfigure;
    }
}