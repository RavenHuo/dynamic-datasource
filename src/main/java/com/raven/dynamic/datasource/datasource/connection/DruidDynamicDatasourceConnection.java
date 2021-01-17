package com.raven.dynamic.datasource.datasource.connection;

import com.alibaba.druid.pool.*;
import com.raven.dynamic.datasource.datasource.statement.DruidDynamicDataSourcePreparedStatement;
import com.raven.dynamic.datasource.transaction.DynamicDatasourceTransactionType;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-12 10:56
 */
public class DruidDynamicDatasourceConnection extends AbstractDruidDynamicDatasourceAdapter {

    private Map<String, DataSource> dataSourceMap;

    private DataSource defaultDatasource;

    public DruidDynamicDatasourceConnection(Map<String, DataSource> dataSourceMap, DruidConnectionHolder holder, DynamicDatasourceTransactionType transactionType, DataSource defaultDatasource) {
        super(holder, transactionType);
        this.dataSourceMap = dataSourceMap;
        this.defaultDatasource = defaultDatasource;
    }

    @Override
    protected Map<String, DataSource> getDataSourceMap() {
        return this.dataSourceMap;
    }

    @Override
    protected DataSource getDataSourceByName(String dataSourceName) {
        if (StringUtils.isBlank(dataSourceName) || null == dataSourceMap || !dataSourceMap.containsKey(dataSourceName)) {
            return defaultDatasource;
        }
        return dataSourceMap.get(dataSourceName);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, getCatalog(), PreparedStatementPool.MethodType.M1);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        DruidDynamicDataSourcePreparedStatement rtnVal = new DruidDynamicDataSourcePreparedStatement(this, stmtHolder);

        holder.addTrace(rtnVal);

        return rtnVal;
    }

    public final void beforeExecute() {
        getConnection();
    }

    public void afterExecute() {
        final DruidConnectionHolder holder = this.holder;
        holder.clearStatementCache();
    }
}
