package com.raven.dynamic.datasource.transaction.execute;

import java.sql.SQLException;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 11:17
 */
public interface ExecuteCallback<T> {
    /**
     * 定义抽象接口，内容由实际场景实现
     * @param target
     * @throws SQLException
     */
    void execute(T target) throws SQLException;
}