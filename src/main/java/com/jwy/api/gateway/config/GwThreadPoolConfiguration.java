/*
 * easy come, easy go.
 *
 * contact : syiae.jwy@gmail.com
 *
 * · · · · ||   ..     __       ___      ____  ®
 * · · · · ||  ||  || _ ||   ||    ||   ||      ||
 * · · · · ||  ||  \\_ ||_.||    ||   \\_  ||
 * · · _//                                       ||
 * · · · · · · · · · · · · · · · · · ·· ·    ___//
 */
package com.jwy.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>
 *     线程池配置
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/19
 */
@EnableAsync
@EnableScheduling
@Configuration
public class GwThreadPoolConfiguration {

    @Bean
    @Primary
    public TaskExecutor primaryTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    //public ThreadPoolTaskExecutor samplePoolExecutor() {
    //    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    //
    //    taskExecutor.setCorePoolSize(2);
    //    taskExecutor.setMaxPoolSize(10);
    //    taskExecutor.setQueueCapacity(0);
    //    taskExecutor.setKeepAliveSeconds(120);
    //    taskExecutor.setThreadNamePrefix("SampleExecutor--");
    //    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    //    taskExecutor.setAwaitTerminationSeconds(60);
    //    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    //    taskExecutor.initialize();
    //    return taskExecutor;
    //}

}
