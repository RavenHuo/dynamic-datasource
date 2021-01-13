package com.raven.dynamic.datasource.datasource.connection;

import com.raven.dynamic.datasource.transaction.wrapper.JdbcMethodInvocation;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-13 15:11
 */
public interface WrapperAdapter {

    final Collection<JdbcMethodInvocation> jdbcMethodInvocations = new ArrayList<>();

    /**
     * record method invocation.
     *
     * @param targetClass target class
     * @param methodName method name
     * @param argumentTypes argument types
     * @param arguments arguments
     */
    @SneakyThrows
    default void recordMethodInvocation(final Class<?> targetClass, final String methodName, final Class<?>[] argumentTypes, final Object[] arguments) {
        jdbcMethodInvocations.add(new JdbcMethodInvocation(targetClass.getMethod(methodName, argumentTypes), arguments));
    }

    /**
     * Replay methods invocation.
     *
     * @param target target object
     */
    default void replayMethodsInvocation(final Object target) {
        for (JdbcMethodInvocation each : jdbcMethodInvocations) {
            each.invoke(target);
        }
    }
}
