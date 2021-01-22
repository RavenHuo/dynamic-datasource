package com.raven.dynamic.datasource.plugin.executor;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * 使用 TTLThreadPoolTaskExecutor 作为默认线程池
 * @author raven
 */
@Data
@EnableAsync(proxyTargetClass = true)
public class TTLThreadPoolConfig {
    /**
     *  线程池维护线程的最小数量.
     */
    @Value("${threadPool.corePoolSize:20}")
    private int corePoolSize;
    /**
     *  线程池维护线程的最大数量
     */
    @Value("${threadPool.maxPoolSize:60}")
    private int maxPoolSize;
    /**
     *  队列最大长度
     */
    @Value("${threadPool.queueCapacity:100}")
    private int queueCapacity;
    /**
     *  线程池前缀
     */
    @Value("${threadPool.threadNamePrefix:ttlExecutor-}")
    private String threadNamePrefix;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new TTLThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        /*
           rejection-policy：当pool已经达到max size的时候，如何处理新任务
           CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
