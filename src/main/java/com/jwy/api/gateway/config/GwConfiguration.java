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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jwy.medusa.common.listener.AppStartedListener;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     系统配置.
 *
 *     取决于网关性质（使用的是WebFlux，而框架使用的是WebMvc），不使用框架公共内容定义，这里需要自己设定
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/8
 */
@Configuration(proxyBeanMethods = false)
public class GwConfiguration {

    @Value("${sys.gw.webclient.connect.timeout:10000}")
    private int connectTimeout;
    @Value("${sys.gw.webclient.timeout:10000}")
    private int readTimeout;

    @Bean
    public AppStartedListener appStartedListener(){
        return new AppStartedListener();
    }

    //json custom
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .timeZone(TimeZone.getTimeZone("GMT+8"));
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webclient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .responseTimeout(Duration.ofMillis(readTimeout))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
