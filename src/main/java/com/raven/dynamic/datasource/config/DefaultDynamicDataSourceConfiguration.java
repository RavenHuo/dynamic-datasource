package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.datasource.DynamicDruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource primaryDatasource(DataSourceProperties properties) throws ClassNotFoundException {
        return createDataSource(properties, HikariDataSource.class);
    }

    @Bean(name = "dynamicDruidDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DynamicDruidDataSource getDataSource(@Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME) DataSource primaryDatasource, DataSourceProperties properties) throws SQLException {
        log.info("dynamicDataSource loading--------------------");
        DynamicDruidDataSource dynamicDruidDataSource = createDataSource(properties, DynamicDruidDataSource.class);
        Map<String, DataSource> defaultDataSource = new HashMap<>(1);
        defaultDataSource.put("default", primaryDatasource);
        dynamicDruidDataSource.afterPropertiesSet(defaultDataSource);
        dynamicDruidDataSource.setDefaultDatasource(primaryDatasource);
        return dynamicDruidDataSource;
    }
}
