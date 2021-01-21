package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 11:40
 */
@Configuration
@Slf4j
public class DefaultDynamicDataSourceConfiguration implements DynamicDataSourceFactory {
    @Value("${dynamic.datasource.className:com.alibaba.druid.pool.DruidDataSource}")
    private String datasourceClassName;

    @Bean(name = DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties("spring.datasource")
    public DataSource primaryDatasource(DataSourceProperties properties) throws ClassNotFoundException {
        return createDataSource(properties, changeDataSourceClass(datasourceClassName));
    }

    @Bean(name = "dynamicDruidDataSource")
    @Primary
    public DynamicDataSource getDataSource(@Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME) DataSource primaryDatasource) throws SQLException {
        log.info("dynamicDataSource loading--------------------");
        DynamicDataSource dynamicDruidDataSource = new DynamicDataSource();
        Map<String, DataSource> defaultDataSource = new HashMap<>(1);
        defaultDataSource.put("default", primaryDatasource);
        dynamicDruidDataSource.setDataSourceMap(defaultDataSource);
        dynamicDruidDataSource.setDefaultDataSource(primaryDatasource);
        return dynamicDruidDataSource;
    }
}
