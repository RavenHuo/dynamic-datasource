package com.raven.dynamic.datasource.properties;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.AbstractDynamicDataSourceFactory;
import com.raven.dynamic.datasource.config.DataSourceProperties;
import com.raven.dynamic.datasource.config.DynamicDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 15:27
 */
@Component
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_IMPL_TYPE, havingValue = DynamicSourceConstant.DYNAMIC_PROPERTIES_IMPL_TYPE)
@EnableConfigurationProperties(PropertiesDataSourceConfigProperties.class)
@Slf4j
public class DynamicDataSourcePropertiesAutoConfiguration extends AbstractDynamicDataSourceFactory<DynamicDataSourceProperties> {

    @Autowired
    private DynamicDataSource dynamicDataSourceRouting;

    @Autowired
    private PropertiesDataSourceConfigProperties propertiesDataSourceConfigProperties;

    @Value("${dynamic.datasource.className:com.alibaba.druid.pool.DruidDataSource}")
    private String datasourceClassName;

    @Override
    @PostConstruct
    public void init() throws ClassNotFoundException {
        loadDataSource(dynamicDataSourceRouting, propertiesDataSourceConfigProperties.getDatasource(), datasourceClassName);
    }

    /**
     * 加载 数据源
     *
     * @return
     */
    @Override
    public List<DynamicDataSourceProperties> loadDataSourcePropertiesList(List<DynamicDataSourceProperties> dynamicDataSourceProperties) {
        log.info("load  DataSourceProperties from properties-------------------");
        checkDataSourceProperties(dynamicDataSourceProperties);
        return dynamicDataSourceProperties;
    }



}
