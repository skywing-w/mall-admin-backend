package com.mall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置。
 * <p>
 * 应用开启了 {@code @EnableAsync}，但不配置线程池时 Spring Boot 会使用
 * {@code SimpleAsyncTaskExecutor}——每次调用都新建线程且不设上限，高频写日志会拖垮 JVM。
 * 这里为操作日志单独准备一个「有界队列 + 调用方执行」的线程池：
 * 队列满时退化为同步执行，宁可拖慢一点也不能丢日志或 OOM。
 *
 * @author wwty
 */
@Slf4j
@Configuration
public class AsyncConfig {

    /** 操作日志异步线程池的 Bean 名称，供 {@code @Async} 引用 */
    public static final String LOG_EXECUTOR = "logExecutor";

    /** 核心线程数：日志写入是 IO 型任务，少量线程即可 */
    private static final int CORE_POOL_SIZE = 2;

    /** 最大线程数 */
    private static final int MAX_POOL_SIZE = 4;

    /** 队列容量：积压超过该值触发拒绝策略 */
    private static final int QUEUE_CAPACITY = 500;

    /** 空闲线程存活时间（秒） */
    private static final int KEEP_ALIVE_SECONDS = 60;

    @Bean(LOG_EXECUTOR)
    public Executor logExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix("op-log-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 关闭时等待已提交任务执行完，避免停机丢日志
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        log.info("操作日志线程池初始化完成：core={} max={} queue={}", CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_CAPACITY);
        return executor;
    }
}