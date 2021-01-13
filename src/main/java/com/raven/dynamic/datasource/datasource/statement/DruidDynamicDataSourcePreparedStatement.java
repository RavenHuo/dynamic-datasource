package com.raven.dynamic.datasource.datasource.statement;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.alibaba.druid.pool.DruidPooledResultSet;
import com.alibaba.druid.pool.PreparedStatementHolder;
import com.raven.dynamic.datasource.datasource.connection.DruidDynamicDatasourceConnection;
import net.bytebuddy.asm.Advice;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-13 11:41
 */
public class DruidDynamicDataSourcePreparedStatement extends DruidPooledPreparedStatement {

    DruidDynamicDatasourceConnection connection;

    public DruidDynamicDataSourcePreparedStatement(DruidDynamicDatasourceConnection connection, PreparedStatementHolder holder) throws SQLException {
        super(connection, holder);
        conn = connection;
        this.connection = connection;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        checkOpen();

        incrementExecuteQueryCount();
        transactionRecord(getSql());

        conn = connection.getConnection();
        oracleSetRowPrefetch();

        try {
            ResultSet rs = getRawStatement().executeQuery();

            if (rs == null) {
                return null;
            }

            DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            addResultSetTrace(poolableResultSet);

            return poolableResultSet;
        } catch (Throwable t) {
            errorCheck(t);

            throw checkException(t);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        checkOpen();

        incrementExecuteUpdateCount();
        transactionRecord(getSql());

        conn = connection.getConnection();
        try {
            return getRawStatement().executeUpdate();
        } catch (Throwable t) {
            errorCheck(t);

            throw checkException(t);
        }
    }
}
