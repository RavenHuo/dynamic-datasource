package com.raven.dynamic.datasource.datasource.provider.properties;

import com.raven.dynamic.datasource.datasource.DynamicDataSourceProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 装配数据源配置信息数组
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dynamic")
public class PropertiesDataSourceConfigProperties {

    private List<DynamicDataSourceProperties> datasource;

}
