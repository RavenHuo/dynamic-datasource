package com.raven.dynamic.datasource.datasource;

import com.raven.dynamic.datasource.common.exception.DynamicSourceException;
import com.raven.dynamic.datasource.config.listener.DynamicDatasourceRefreshEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.Table;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-21 21:28
 */
@Slf4j
@Component
public class DynamicDataSourceOperation extends DynamicDataSourceProvider{

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public synchronized void addDataSource(String dbTag, DataSource dataSource) {

        Map<String, DataSource> dataSourceMap = dynamicDataSource.getDataSourceMap();
        if (dataSourceMap.containsKey(dbTag)) {
            return;
        }

        dataSourceMap.put(dbTag, dataSource);
        log.info("addDynamicDataSource ----- dbTag={}", dbTag);
        applicationEventPublisher.publishEvent(new DynamicDatasourceRefreshEvent(dataSourceMap));
    }

}
