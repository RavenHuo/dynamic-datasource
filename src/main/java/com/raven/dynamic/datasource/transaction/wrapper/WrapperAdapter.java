package com.raven.dynamic.datasource.transaction.wrapper;

import lombok.SneakyThrows;

import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 14:52
 */
public class WrapperAdapter implements Wrapper {

    private final Collection<JdbcMethodInvocation> jdbcMethodInvocations = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
    }

    @Override
    public final boolean isWrapperFor(final Class<?> iface) {
        return iface.isInstance(this);
    }

    /**
     * record method invocation.
     *
     * @param targetClass target class
     * @param methodName method name
     * @param argumentTypes argument types
     * @param arguments arguments
     */
    @SneakyThrows
    public final void recordMethodInvocation(final Class<?> targetClass, final String methodName, final Class<?>[] argumentTypes, final Object[] arguments) {
        jdbcMethodInvocations.add(new JdbcMethodInvocation(targetClass.getMethod(methodName, argumentTypes), arguments));
    }

    /**
     * Replay methods invocation.
     *
     * @param target target object
     */
    public final void replayMethodsInvocation(final Object target) {
        for (JdbcMethodInvocation each : jdbcMethodInvocations) {
            each.invoke(target);
        }
    }

}