package com.raven.dynamic.datasource.properties;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.AbstractDynamicDataSourceFactory;
import com.raven.dynamic.datasource.config.DynamicDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DynamicDataSourcePropertiesAutoConfiguration extends AbstractDynamicDataSourceFactory {

    @Autowired
    private DynamicDataSource dynamicDataSourceRouting;

    @Autowired
    private PropertiesDataSourceConfigProperties propertiesDataSourceConfigProperties;

    @Override
    @PostConstruct
    public void init() {
        super.initDynamicDataSource(dynamicDataSourceRouting, loadDataSourceProperties());
    }

    /**
     * 加载 数据源
     *
     * @return
     */
    @Override
    public List<DynamicDataSourceProperties> loadDataSourceProperties() {
        log.info("load  DataSourceProperties from properties-------------------");
        List<DynamicDataSourceProperties> dynamicDataSourceProperties = propertiesDataSourceConfigProperties.getDatasource();
        super.checkDataSourceProperties(dynamicDataSourceProperties);
        return dynamicDataSourceProperties;
    }

}
