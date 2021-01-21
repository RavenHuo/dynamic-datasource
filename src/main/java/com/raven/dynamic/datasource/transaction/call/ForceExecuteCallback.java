package com.raven.dynamic.datasource.transaction.call;

import java.sql.SQLException;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-21 00:04
 */
public interface ForceExecuteCallback<T> {

    /**
     * Execute.
     *
     * @param target target to be executed
     * @throws SQLException SQL exception
     */
    void execute(T target) throws SQLException;
}