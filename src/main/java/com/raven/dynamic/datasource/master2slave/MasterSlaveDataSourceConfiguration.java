package com.raven.dynamic.datasource.master2slave;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:37
 */
@EnableConfigurationProperties
@Configuration
@Data
@ConfigurationProperties(prefix = "dynamic.datasource")
public class MasterSlaveDataSourceConfiguration {

    private DataSourceProperties master;

    private DataSourceProperties slave;
}
