package com.raven.dynamic.datasource.plugin;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.plugin.executor.TTLThreadPoolConfig;
import com.raven.dynamic.datasource.plugin.feign.FeignRequestHeaderInterceptor;
import com.raven.dynamic.datasource.plugin.hystrix.DynamicDataSourceContextConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-22 00:47
 */
@Slf4j
@Component
@ConditionalOnProperty(value = DynamicSourceConstant.DYNAMIC_DATASOURCE_CLOUD_SWITCH, matchIfMissing = false)
public class DynamicDataSourceSpringCloudAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("DynamicDataSourceSpringCloudAutoConfiguration init ------------");
    }

    @Bean
    public TTLThreadPoolConfig ttlThreadPoolConfig() {
        return new TTLThreadPoolConfig();
    }

    @Bean
    public FeignRequestHeaderInterceptor feignRequestHeaderInterceptor() {
        return new FeignRequestHeaderInterceptor();
    }

    @Configuration
    @ConditionalOnClass({ Hystrix.class })
    public static class HystrixDynamicDataSourceContextConfiguration {

        @Autowired(required = false)
        private HystrixConcurrencyStrategy existingConcurrencyStrategy;

        @PostConstruct
        public void init() {
            // Keeps references of existing Hystrix plugins.
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();

            HystrixPlugins.reset();

            // Registers existing plugins excepts the Concurrent Strategy plugin.
            HystrixPlugins.getInstance()
                    .registerConcurrencyStrategy(new DynamicDataSourceContextConcurrencyStrategy(existingConcurrencyStrategy));
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
        }

    }
}
