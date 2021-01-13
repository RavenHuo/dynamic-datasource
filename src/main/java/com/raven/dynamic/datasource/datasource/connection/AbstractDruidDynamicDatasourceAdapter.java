package com.raven.dynamic.datasource.datasource.connection;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.DynamicDatasourceTransactionType;
import com.raven.dynamic.datasource.transaction.execute.ExecuteCallback;
import com.raven.dynamic.datasource.transaction.execute.ExecuteTemplate;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 21:44
 */
public abstract class AbstractDruidDynamicDatasourceAdapter extends DruidPooledConnection implements WrapperAdapter {


    public AbstractDruidDynamicDatasourceAdapter(DruidConnectionHolder holder, DynamicDatasourceTransactionType transactionType) {
        super(holder);
        this.transactionType = transactionType;
    }

    private final Multimap<String, Connection> cachedConnections = LinkedHashMultimap.create();

    private final ExecuteTemplate<Connection> forceExecuteTemplate = new ExecuteTemplate<>();

    private final ExecuteTemplate<Map.Entry<String, Connection>> forceExecuteTemplateForClose = new ExecuteTemplate<>();

    private DynamicDatasourceTransactionType transactionType;

    private boolean autoCommit;

    @SneakyThrows
    @Override
    public DruidPooledConnection getConnection() {
        conn = getConnection(LocalDynamicDataSourceHolder.getDbTag());
        if (!getAutoCommit()) {
            createTransactionInfo();
        }
        return new DruidPooledConnection(holder);
    }

    /**
     * 获取数据域
     * @param dataSourceName
     * @return
     * @throws SQLException
     */
    public Connection getConnection(final String dataSourceName) throws SQLException {
        return getConnectionList(dataSourceName, 1).get(0);
    }

    private List<Connection> getConnectionList(final String dataSourceName, final int connectionSize) throws SQLException {
        DataSource dataSource = getDataSourceByName(dataSourceName);
        Preconditions.checkState(null != dataSource, "Missing the data source name: '%s'", dataSourceName);
        Collection<Connection> connections;
        synchronized (cachedConnections) {
            connections = cachedConnections.get(dataSourceName);
        }
        List<Connection> result;
        if (connections.size() >= connectionSize) {
            result = new ArrayList<>(connections).subList(0, connectionSize);
        } else if (!connections.isEmpty()) {
            result = new ArrayList<>(connectionSize);
            result.addAll(connections);
            List<Connection> newConnections = createConnections(dataSource, connectionSize - connections.size());
            result.addAll(newConnections);
            synchronized (cachedConnections) {
                cachedConnections.putAll(dataSourceName, newConnections);
            }
        } else {
            result = new ArrayList<>(createConnections(dataSource, connectionSize));
            synchronized (cachedConnections) {
                cachedConnections.putAll(dataSourceName, result);
            }
        }
        return result;
    }

    private List<Connection> createConnections(final DataSource dataSource, final int connectionSize) throws SQLException {
        List<Connection> result = new ArrayList<>(connectionSize);
        for (int i = 0; i < connectionSize; i++) {
            try {
                result.add(createConnection(dataSource));
            } catch (final SQLException ex) {
                for (Connection each : result) {
                    each.close();
                }
                throw new SQLException(String.format("Could't get %d connections one time, partition succeed connection(%d) have released!", connectionSize, result.size()), ex);
            }
        }
        return result;
    }

    private Connection createConnection(final DataSource dataSource) throws SQLException {
        Connection result = dataSource.getConnection();
        replayMethodsInvocation(result);
        return result;
    }


    @Override
    public boolean getAutoCommit() {
        return autoCommit;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        recordMethodInvocation(Connection.class, "setAutoCommit", new Class[]{boolean.class}, new Object[]{autoCommit});
        if (DynamicDatasourceTransactionType.LOCAL == transactionType) {
            forceExecuteTemplate.execute(cachedConnections.values(), new ExecuteCallback<Connection>() {
                @Override
                public void execute(final Connection connection) throws SQLException {
                    connection.setAutoCommit(autoCommit);
                }
            });
        }
        if (autoCommit) {
            return;
        }
    }

    @Override
    public void commit() throws SQLException {
        if (DynamicDatasourceTransactionType.LOCAL.equals(transactionType)) {
            forceExecuteTemplate.execute(cachedConnections.values(), new ExecuteCallback<Connection>() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    connection.commit();
                }
            });
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (DynamicDatasourceTransactionType.LOCAL.equals(transactionType)) {
            forceExecuteTemplate.execute(cachedConnections.values(), new ExecuteCallback<Connection>() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    connection.rollback();
                }
            });
        }
    }

    @Override
    public void close() throws SQLException{
        closed = true;
        try {
            forceExecuteTemplateForClose.execute(cachedConnections.entries(), new ExecuteCallback<Map.Entry<String, Connection>>() {
                @Override
                public void execute(final Map.Entry<String, Connection> cachedConnections) throws SQLException {
                    cachedConnections.getValue().close();
                }
            });
        } finally {
            cachedConnections.clear();
        }
    }

    protected abstract Map<String, DataSource> getDataSourceMap();

    protected abstract DataSource getDataSourceByName(String dataSourceName) throws SQLException;
}
