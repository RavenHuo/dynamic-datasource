package com.raven.dynamic.datasource.transaction.statement;

import com.raven.dynamic.datasource.config.DynamicDataSource;
import com.raven.dynamic.datasource.transaction.ConnectionFactory;

import java.sql.*;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-19 23:34
 */
public class DynamicDataSourceStatement implements Statement {

    private Statement stmt;

    public DynamicDataSourceStatement(DynamicDataSource dynamicDataSource) throws SQLException{
        Connection connection = ConnectionFactory.getConnectionProxy(dynamicDataSource);
        stmt = connection.createStatement();
    }

    public DynamicDataSourceStatement(DynamicDataSource dynamicDataSource, int resultSetType, int resultSetConcurrency) throws SQLException{
        Connection connection = ConnectionFactory.getConnectionProxy(dynamicDataSource);
        stmt = connection.createStatement(resultSetType, resultSetConcurrency);
    }


    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return stmt.executeQuery(sql);
    }


    @Override
    public int executeUpdate(String sql) throws SQLException {
        return stmt.executeUpdate(sql);
    }


    @Override
    public void close() throws SQLException {
        stmt.close();
    }


    @Override
    public int getMaxFieldSize() throws SQLException {
        return stmt.getMaxFieldSize();
    }


    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        stmt.setMaxFieldSize(max);
    }


    @Override
    public int getMaxRows() throws SQLException {
        return stmt.getMaxRows();
    }


    @Override
    public void setMaxRows(int max) throws SQLException {
        stmt.setMaxRows(max);
    }


    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        stmt.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return stmt.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        stmt.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        stmt.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return stmt.getWarnings();
    }


    @Override
    public void clearWarnings() throws SQLException {
        stmt.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        stmt.setCursorName(name);
    }


    @Override
    public boolean execute(String sql) throws SQLException {
        return stmt.execute(sql);
    }


    @Override
    public ResultSet getResultSet() throws SQLException {
        return stmt.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return stmt.getUpdateCount();
    }


    @Override
    public boolean getMoreResults() throws SQLException {
        return stmt.getMoreResults();
    }


    @Override
    public void setFetchDirection(int direction) throws SQLException {
        stmt.setFetchDirection(direction);
    }


    @Override
    public int getFetchDirection() throws SQLException {
        return stmt.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        stmt.setFetchSize(rows);
    }


    @Override
    public int getFetchSize() throws SQLException {
        return stmt.getFetchSize();
    }

    /**
     * Retrieves the result set concurrency for <code>ResultSet</code> objects
     * generated by this <code>Statement</code> object.
     *
     * @return either <code>ResultSet.CONCUR_READ_ONLY</code> or
     * <code>ResultSet.CONCUR_UPDATABLE</code>
     * @throws SQLException if a database access error occurs or
     *                      this method is called on a closed <code>Statement</code>
     * @since 1.2
     */
    @Override
    public int getResultSetConcurrency() throws SQLException {
        return stmt.getResultSetConcurrency();
    }

    /**
     * Retrieves the result set type for <code>ResultSet</code> objects
     * generated by this <code>Statement</code> object.
     *
     * @return one of <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     * <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     * <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @throws SQLException if a database access error occurs or
     *                      this method is called on a closed <code>Statement</code>
     * @since 1.2
     */
    @Override
    public int getResultSetType() throws SQLException {
        return stmt.getResultSetType();
    }

    /**
     * Adds the given SQL command to the current list of commands for this
     * <code>Statement</code> object. The commands in this list can be
     * executed as a batch by calling the method <code>executeBatch</code>.
     * <p>
     * <strong>Note:</strong>This method cannot be called on a
     * <code>PreparedStatement</code> or <code>CallableStatement</code>.
     *
     * @param sql typically this is a SQL <code>INSERT</code> or
     *            <code>UPDATE</code> statement
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the
     *                      driver does not support batch updates, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @see #executeBatch
     * @see DatabaseMetaData#supportsBatchUpdates
     * @since 1.2
     */
    @Override
    public void addBatch(String sql) throws SQLException {
        stmt.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        stmt.clearBatch();
    }


    @Override
    public int[] executeBatch() throws SQLException {
        return stmt.executeBatch();
    }


    // TODO 修改
    @Override
    public Connection getConnection() throws SQLException {
        return stmt.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return stmt.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return stmt.getGeneratedKeys();
    }


    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return stmt.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return stmt.executeUpdate(sql, columnIndexes);
    }

    // TODO
    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return stmt.executeUpdate(sql, columnNames);
    }

    // TODO
    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return stmt.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return stmt.execute(sql, columnIndexes);
    }


    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return stmt.execute(sql, columnNames);
    }


    @Override
    public int getResultSetHoldability() throws SQLException {
        return stmt.getResultSetHoldability();
    }


    @Override
    public boolean isClosed() throws SQLException {
        return stmt.isClosed();
    }


    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        stmt.setPoolable(poolable);
    }


    @Override
    public boolean isPoolable() throws SQLException {
        return stmt.isPoolable();
    }


    @Override
    public void closeOnCompletion() throws SQLException {
        stmt.closeOnCompletion();
    }


    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return stmt.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return stmt.unwrap(iface);
    }


    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return stmt.isWrapperFor(iface);
    }
}
