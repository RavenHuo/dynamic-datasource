package com.raven.dynamic.datasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:46
 */
public interface DynamicDataSourceFactory {


    @SuppressWarnings("unchecked")
    default  <T> T createDataSource(DataSourceProperties properties,
                                            Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
}
