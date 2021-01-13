package com.raven.dynamic.datasource.datasource;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceAfterRefresh;
import com.raven.dynamic.datasource.datasource.connection.DruidDynamicDatasourceConnection;
import com.raven.dynamic.datasource.transaction.DynamicDatasourceTransactionType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-12 11:15
 */
public class DynamicDruidDataSource extends DruidDataSource implements DynamicDataSourceAfterRefresh {

    private static final long serialVersionUID = -225075440695924728L;

    private Map<String, DataSource> dataSourceMap;

    private DynamicDatasourceTransactionType transactionType;

    private DataSource defaultDatasource;
    private DynamicDruidDataSource() {
        this.transactionType = DynamicDatasourceTransactionType.LOCAL;
    }

    @Override
    public DruidDynamicDatasourceConnection getConnection() throws SQLException {
        init();
        return new DruidDynamicDatasourceConnection(dataSourceMap, new DruidConnectionHolder(this, createPhysicalConnection()), this.transactionType, defaultDatasource);
    }

    public static DynamicDruidDataSource createDataSource() {
        return new DynamicDruidDataSource();
    }

    /**
     * 刷新方法
     *
     * @throws SQLException
     */
    @Override
    public void afterPropertiesSet(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    @Override
    public void setDefaultDatasource(DataSource datasource) {
        this.defaultDatasource = datasource;
    }
}
