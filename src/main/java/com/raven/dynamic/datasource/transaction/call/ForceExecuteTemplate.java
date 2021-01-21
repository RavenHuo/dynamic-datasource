package com.raven.dynamic.datasource.transaction.call;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-21 00:04
 */
public final class ForceExecuteTemplate<T> {

    /**
     * Force execute.
     *
     * @param targets targets to be executed
     * @param callback force execute callback
     * @throws SQLException throw SQL exception after all targets are executed
     */
    public void execute(final Collection<T> targets, final ForceExecuteCallback<T> callback) throws SQLException {
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
            ex.setNextException(each);
        }
        throw ex;
    }
}

