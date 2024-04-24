//package com.example.SS2_Backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
//@EnableAsync
//@Configuration
//public class AsyncConfig {
//
//    @Bean(name = "taskExecutor")
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
//        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
//        executor.setQueueCapacity(20);
//        executor.setThreadNamePrefix("Async-");
//        executor.initialize();
//        return executor;
//    }
//}
