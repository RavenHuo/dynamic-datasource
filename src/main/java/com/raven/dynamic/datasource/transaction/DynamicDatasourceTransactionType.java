package com.raven.dynamic.datasource.transaction;


/**
 * @description:
 * @author: huorw
 * @create: 2021-01-10 23:00
 */
public enum DynamicDatasourceTransactionType {
    /**
     * NONE：not use this Transaction
     * LOCAL: default
     * XA: XA Transaction
     * SAGA: SAGA Transaction
     */
    NONE, LOCAL, XA, SAGA
}
