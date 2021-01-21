package com.raven.dynamic.datasource.config.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-12 18:52
 */
public class DynamicDataSourceContext {

    public Map<Object, Object> dataSourceMap;

    DynamicDataSourceContext() {
        dataSourceMap = new ConcurrentHashMap<>();
    }
}
