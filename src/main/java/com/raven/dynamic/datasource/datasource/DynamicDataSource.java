package com.raven.dynamic.datasource.datasource;

import com.raven.dynamic.datasource.common.constant.DynamicDataSourceConstant;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.DynamicDataSourceConnection;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-19 14:28
 */
public class DynamicDataSource extends DynamicDataSourceRouting {
    @Override
    public String determineCurrentLookupKey() {
        return StringUtils.isBlank(LocalDynamicDataSourceHolder.getDbTag()) ? DynamicDataSourceConstant.DYNAMIC_DATASOURCE_DEFAULT_DATASOURCE_NAME : LocalDynamicDataSourceHolder.getDbTag();
    }

    @Override
    protected synchronized void addDataSource(String dbTag, DataSource dataSource) {
        dataSourceMap.put(dbTag, dataSource);
    }

    /**
     * 刷新
     *
     * @param dataSourceMap
     */
    @Override
    public synchronized void refreshDataSource(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return new DynamicDataSourceConnection(this);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new DynamicDataSourceConnection(this);
    }
}
