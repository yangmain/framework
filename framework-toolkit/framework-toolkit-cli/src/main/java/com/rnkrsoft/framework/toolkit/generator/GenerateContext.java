package com.rnkrsoft.framework.toolkit.generator;

import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by woate on 2018/4/23.
 */
@Data
@Builder
@ToString
public class GenerateContext {
    String packageName;
    TableMetadata tableMetadata;
}
