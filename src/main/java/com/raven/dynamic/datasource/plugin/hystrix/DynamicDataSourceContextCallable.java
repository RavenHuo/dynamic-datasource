package com.raven.dynamic.datasource.plugin.hystrix;

import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;

import java.util.concurrent.Callable;



/**
 * 多数据源线程标识传递类 用于rx.java 的call方法调用
 * @author raven
 */
public final class DynamicDataSourceContextCallable <V> implements Callable<V> {

    private final Callable<V> callable;

    private String dbTag;

    public DynamicDataSourceContextCallable(Callable<V> callable, String dbTag) {
        this.callable = callable;
        this.dbTag = dbTag;
    }

    @Override
    public V call() throws Exception {
        try {
            LocalDynamicDataSourceHolder.setDbTag(dbTag);
            return callable.call();
        } finally {
            LocalDynamicDataSourceHolder.clearDbTag();
        }

    }

}

