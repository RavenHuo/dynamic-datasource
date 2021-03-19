package com.raven.dynamic.datasource.config.listener;

import com.raven.dynamic.datasource.datasource.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: huorw
 * @create: 2021-03-19 15:29
 */
@Component
public class DynamicDatasourceApplicationListener implements ApplicationListener<DynamicDatasourceRefreshEvent> {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(DynamicDatasourceRefreshEvent event) {
        event.refresh(dynamicDataSource);
    }
}
