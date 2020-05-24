package com.raven.dynamic.datasource.config.aspect;

import com.raven.dynamic.datasource.common.annotation.DataSourceSwitcher;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-22 00:06
 */
@Order(-1)
@Component
@Aspect
@Slf4j
public class DsSwitcherAspect {

    @Pointcut("@annotation(com.raven.dynamic.datasource.common.annotation.DataSourceSwitcher)")
    private void pointcut() {}

    /**
     * 在调用前切换数据源
     */
    @Before("pointcut()")
    public void transServiceMethod(JoinPoint joinPoint) {
        switchDataSource(joinPoint);
    }

    /**
     * 在调用后，清空数据源标识
     */
    @After("pointcut()")
    public void after() {
        clearDataSource();
    }
    /**
     * 有异常的情况下，清空数据源标识
     */
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        clearDataSource();
    }

    /**
     * 根据注解改变数据源
     */
    private void switchDataSource(JoinPoint joinPoint) {
        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();
        DataSourceSwitcher switcher = signature.getMethod().getAnnotation(DataSourceSwitcher.class);
        if (!Objects.isNull(switcher) && StringUtils.isNotBlank(switcher.value())) {
            String ds = switcher.value();
            LocalDynamicDataSourceHolder.setDbTag(ds);
        }
    }

    /**
     * 在每次调用之后，清空数据源
     */
    private void clearDataSource() {
        LocalDynamicDataSourceHolder.clearDbTag();
    }
}
