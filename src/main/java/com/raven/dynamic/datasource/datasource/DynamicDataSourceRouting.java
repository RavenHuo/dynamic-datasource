package com.raven.dynamic.datasource.datasource;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-21 17:46
 */
@Data
public abstract class DynamicDataSourceRouting extends AbstractDataSource implements InitializingBean {

    protected volatile Map<String, DataSource> dataSourceMap;

    private DataSource defaultDataSource;

    public abstract String determineCurrentLookupKey();

    protected abstract void addDataSource(String dbTag, DataSource dataSource);

    /**
     * 刷新
     * @param dataSourceMap
     */
    public abstract void refreshDataSource(Map<String, DataSource> dataSourceMap);


    public DataSource determineDataSource(String dbTag) {
        Assert.notNull(this.defaultDataSource, "DataSource router not initialized");
        DataSource dataSource = dataSourceMap.get(dbTag);
        if (dataSource == null || dbTag == null) {
            dataSource = this.getDefaultDataSource();
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine default DataSource for lookup key [" + dbTag + "]");
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
