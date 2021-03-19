package com.raven.dynamic.datasource.config.listener;

import com.raven.dynamic.datasource.datasource.DynamicDataSource;
import org.springframework.context.ApplicationEvent;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2021-03-19 11:59
 */
public class DynamicDatasourceRefreshEvent extends ApplicationEvent {

    private Map<String, DataSource> sourceMap;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DynamicDatasourceRefreshEvent(Map<String, DataSource> sourceMap) {
        super(sourceMap);
        this.sourceMap = sourceMap;
    }


    public void refresh(DynamicDataSource dynamicDataSource) {
        dynamicDataSource.refreshDataSource(sourceMap);
    }

}
