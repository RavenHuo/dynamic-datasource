package com.raven.dynamic.datasource.config.provider.properties;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.datasource.AbstractDynamicDataSourceProvider;
import com.raven.dynamic.datasource.datasource.DynamicDataSource;
import com.raven.dynamic.datasource.datasource.DynamicDataSourceProperties;
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
public class DynamicDataSourcePropertiesAutoConfiguration extends AbstractDynamicDataSourceProvider<DynamicDataSourceProperties> {

    @Autowired
    private PropertiesDataSourceConfigProperties propertiesDataSourceConfigProperties;

    @Value("${dynamic.datasource.className:com.alibaba.druid.pool.DruidDataSource}")
    private String datasourceClassName;

    @Autowired
    private DynamicDataSource dynamicDataSource;


    @Override
    @PostConstruct
    public void init() throws ClassNotFoundException {
        loadDataSource(dynamicDataSource, propertiesDataSourceConfigProperties.getDatasource(), datasourceClassName);
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
