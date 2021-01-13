package com.raven.dynamic.datasource.transaction.adapter;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.DynamicDatasourceTransactionType;
import com.raven.dynamic.datasource.transaction.execute.ExecuteCallback;
import com.raven.dynamic.datasource.transaction.execute.ExecuteTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 15:12
 */
public abstract class AbstractDynamicDatasourceConnectionAdapter extends AbstractDynamicDatasourceConnection{

    private final Multimap<String, Connection> cachedConnections = LinkedHashMultimap.create();

    private final ExecuteTemplate<Connection> forceExecuteTemplate = new ExecuteTemplate<>();

    private DynamicDatasourceTransactionType transactionType;

    public AbstractDynamicDatasourceConnectionAdapter(DynamicDatasourceTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }


    @Override
    public void close() throws SQLException {

    }


    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }


    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

}
