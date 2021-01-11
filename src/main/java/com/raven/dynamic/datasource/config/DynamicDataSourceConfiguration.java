package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

    @Value("${dynamic.datasource.className:com.alibaba.druid.pool.DruidDataSource}")
    private String datasourceClassName;

    @Bean(name = DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties("spring.datasource")
    public DataSource primaryDatasource(DataSourceProperties properties) throws ClassNotFoundException {
        return createDataSource(properties, changeDataSourceClass(datasourceClassName));
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
