package com.wsws.moduleexternalapi.fcm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 코어 스레드 크기
        executor.setMaxPoolSize(5); // 최대 스레드 크기
        executor.setQueueCapacity(10); // 큐 용량
        executor.setThreadNamePrefix("Async-Executor-"); // 스레드 이름
        executor.initialize();
        return executor;
    }
}
