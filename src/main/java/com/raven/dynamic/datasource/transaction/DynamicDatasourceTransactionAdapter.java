package com.raven.dynamic.datasource.transaction;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-10 23:00
 */
public class DynamicDatasourceTransactionAdapter implements InitializingBean {
    @Nullable
    private Map<Object, Object> targetDataSources;

    private boolean lenientFallback = true;

    private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

    @Nullable
    private Map<Object, DataSource> resolvedDataSources;

    protected void setTargetDataSources(@NotNull Map<Object, Object> datasource) {
        this.targetDataSources = datasource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        resolvedDataSources = new ConcurrentHashMap<>();
        Assert.isTrue(targetDataSources == null || targetDataSources.isEmpty(), "targetDataSource is not null");
        targetDataSources.forEach((k, v) -> {
            if (v instanceof DataSource) {
                resolvedDataSources.put(k, (DataSource) v);
            }
        });
    }
}
