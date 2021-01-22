package com.raven.dynamic.datasource.plugin.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * hystrix 策略
 * 通过wrapCallable() 复制和传递线程状态
 * @author raven
 */
public class DynamicDataSourceContextConcurrencyStrategy  extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    public DynamicDataSourceContextConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, threadPoolProperties)
                : super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    /**
     * Provides an opportunity to wrap/decorate a {@code Callable<T>} before execution.
     * <p>
     * This can be used to inject additional behavior such as copying of thread state (such as {@link ThreadLocal}).
     * <p>
     * <b>Default Implementation</b>
     * <p>
     * Pass-thru that does no wrapping.
     *
     * @param callable
     *            {@code Callable<T>} to be executed via a {@link ThreadPoolExecutor}
     * @return {@code Callable<T>} either as a pass-thru or wrapping the one given
     */
    // 复制和传递线程状态
    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy
                .wrapCallable(new DynamicDataSourceContextCallable<>(callable, LocalDynamicDataSourceHolder.getDbTag()))
                : super.wrapCallable(new DynamicDataSourceContextCallable<>(callable, LocalDynamicDataSourceHolder.getDbTag()));
    }

}


