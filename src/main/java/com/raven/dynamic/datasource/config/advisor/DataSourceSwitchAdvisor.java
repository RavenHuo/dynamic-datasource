package com.raven.dynamic.datasource.config.advisor;

import com.raven.dynamic.datasource.common.annotation.DataSourceSwitcher;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.ConnectionFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-21 20:51
 */
public class DataSourceSwitchAdvisor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        DataSourceSwitcher switcher = methodInvocation.getMethod().getAnnotation(DataSourceSwitcher.class);
        if (!Objects.isNull(switcher) && StringUtils.isNotBlank(switcher.value())) {
            String ds = switcher.value();
            LocalDynamicDataSourceHolder.setDbTag(ds);
        }
        Object o;
        try {
            o = methodInvocation.proceed();
        }  finally {
            LocalDynamicDataSourceHolder.clearDbTag();
        }
        return o;
    }
}
