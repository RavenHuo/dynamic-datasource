package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicDataSourceConstant;
import com.raven.dynamic.datasource.config.filter.DynamicDataSourceHeaderFilter;
import com.raven.dynamic.datasource.config.filter.DynamicDataSourceUrlFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 18:40
 */
@Component
@ConditionalOnProperty(value = DynamicDataSourceConstant.DYNAMIC_DATASOURCE_FILTER_SWITCH, havingValue = "true")
@Import({DynamicDataSourceHeaderFilter.class, DynamicDataSourceUrlFilter.class})
@Slf4j
public class DynamicDataSourceFilterAutoConfiguration {


    @PostConstruct
    public void init() {
        log.info("load dynamicDataSource Filter ---------------------------");
    }


}
