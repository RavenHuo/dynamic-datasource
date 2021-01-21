package com.raven.dynamic.datasource.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-19 10:40
 */
public class DynamicDataSourceTransactionAdvisor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        boolean state = true;
        Object o;
        try {
            o = methodInvocation.proceed();
        } catch (Exception e) {
            state = false;
            throw e;
        } finally {
            ConnectionFactory.notify(state);
        }
        return o;
    }
}
