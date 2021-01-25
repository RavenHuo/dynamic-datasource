package com.raven.dynamic.datasource.datasource;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.DynamicDataSourceConnection;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-19 14:28
 */
public class DynamicDataSource extends DynamicDataSourceRouting {
    @Override
    public String determineCurrentLookupKey() {
        return StringUtils.isBlank(LocalDynamicDataSourceHolder.getDbTag()) ? DynamicSourceConstant.DYNAMIC_DATASOURCE_DEFAULT_DATASOURCE_NAME : LocalDynamicDataSourceHolder.getDbTag();
    }

    @Override
    public void addDataSource(String dbTag, DataSource dataSource) {
        dataSourceMap.put(dbTag, dataSource);
    }

    @Override
    public void deleteDataSource(String dbTag) {
        dataSourceMap.remove(dbTag);
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
