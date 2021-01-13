package com.raven.dynamic.datasource.transaction.execute;

import com.raven.dynamic.datasource.transaction.DynamicDatasourceTransactionManager;
import org.springframework.transaction.TransactionSystemException;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 11:16
 */
public class ExecuteTemplate<T> {

    public void execute(final Collection<T> targets, final ExecuteCallback<T> callback) throws SQLException {
        Collection<SQLException> exceptions = new LinkedList<>();
        for (T each : targets) {
            try {
                callback.execute(each);
            } catch (final SQLException ex) {
                exceptions.add(ex);
            }
        }
        throwSQLExceptionIfNecessary(exceptions);
    }

    private void throwSQLExceptionIfNecessary(final Collection<SQLException> exceptions) throws SQLException {
        if (exceptions.isEmpty()) {
            return;
        }
        SQLException ex = new SQLException();
        for (SQLException each : exceptions) {
            ex.addSuppressed(each);
        }
        throw ex;
    }
}
