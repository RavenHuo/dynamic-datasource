package com.raven.dynamic.datasource.datasource;


import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * 数据源配置信息
 */
@Data
public class DynamicDataSourceProperties extends DataSourceProperties {

    /**
     * 数据库标志
     */
    private String dataSourceTag;

}
