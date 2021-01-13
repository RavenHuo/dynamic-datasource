package com.raven.dynamic.datasource.transaction.wrapper;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 14:54
 */
@Data
public class JdbcMethodInvocation {

    @Getter
    private final Method method;

    @Getter
    private final Object[] arguments;

    @SneakyThrows
    public void invoke(final Object target) {
        method.invoke(target, arguments);
    }
}
