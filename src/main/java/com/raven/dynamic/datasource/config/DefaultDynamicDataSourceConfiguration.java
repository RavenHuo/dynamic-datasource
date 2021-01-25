package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.datasource.DynamicDataSource;
import com.raven.dynamic.datasource.datasource.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 11:40
 */
@Component
@Slf4j
public class DefaultDynamicDataSourceConfiguration extends DynamicDataSourceProvider {

    @Value("${dynamic.datasource.className:com.alibaba.druid.pool.DruidDataSource}")
    private String datasourceClassName;

    @Bean(name = DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties("spring.datasource")
    public DataSource primaryDatasource(DataSourceProperties properties) throws ClassNotFoundException {
        return createDataSource(properties, changeDataSourceClass(datasourceClassName));
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dynamicDataSource(@Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME) DataSource primaryDatasource) throws SQLException {
        log.info("default   dynamicDataSource loading--------------------");
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<String, DataSource> defaultDataSource = new ConcurrentHashMap<>(8);
        defaultDataSource.put(DynamicSourceConstant.DYNAMIC_DATASOURCE_DEFAULT_DATASOURCE_NAME, primaryDatasource);
        dynamicDataSource.setDataSourceMap(defaultDataSource);
        dynamicDataSource.setDefaultDataSource(primaryDatasource);
        return dynamicDataSource;
    }
}
