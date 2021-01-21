package com.raven.dynamic.datasource.datasource;

import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.ConnectionFactory;
import com.raven.dynamic.datasource.transaction.ConnectionProxy;
import com.raven.dynamic.datasource.transaction.DynamicDataSourceConnection;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-21 17:46
 */
@Data
public abstract class DynamicDataSourceRouting extends AbstractDataSource implements InitializingBean {

    protected Map<String, DataSource> dataSourceMap;

    private DataSource defaultDataSource;

    public abstract String determineCurrentLookupKey();

    public abstract void addDataSource(String dbTag, DataSource dataSource);

    public abstract void deleteDataSource(String dbTag, DataSource dataSource);


    public DataSource determineDataSource(String dbTag) {
        Assert.notNull(this.defaultDataSource, "DataSource router not initialized");
        DataSource dataSource = dataSourceMap.get(dbTag);
        if (dataSource == null || dbTag == null) {
            dataSource = this.getDefaultDataSource();
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + dbTag + "]");
        }
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.defaultDataSource == null) {
            throw new IllegalArgumentException("defaultDataSource is required");
        }
    }
}
