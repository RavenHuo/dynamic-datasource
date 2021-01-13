package com.raven.dynamic.datasource.config;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-19 11:42
 */
public interface DynamicDataSourceAfterRefresh {

    /**
     * 刷新方法
     * @throws SQLException
     */
    void afterPropertiesSet(Map<String, DataSource> dataSourceMap);

    void setDefaultDatasource(DataSource datasource);
}
