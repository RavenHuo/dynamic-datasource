package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 11:40
 */
@Configuration
public class DynamicDataSourceConfiguration implements DynamicDataSourceFactory {

    @Bean(name = DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource primaryDatasource(DataSourceProperties properties) {
        HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
        return dataSource;
    }


    @Bean(name = DynamicSourceConstant.DYNAMIC_DATASOURCE_ROUTING)
    @Primary
    public DynamicDataSource dynamicDataSourceRouting(@Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)DataSource primaryDatasource) {
        Map<Object, Object> target = new HashMap<>();
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(primaryDatasource);
        dataSource.setTargetDataSources(target);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

}
