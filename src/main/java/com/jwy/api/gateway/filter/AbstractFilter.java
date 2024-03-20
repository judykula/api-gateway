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
package com.jwy.api.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwy.medusa.mvc.MyResponse;
import com.jwy.medusa.mvc.MyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 *     定义根拦截器实现，主要包含公共内容
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/12
 */
@Slf4j
abstract class AbstractFilter implements GlobalFilter, Ordered, FilterOrderDefinition{

    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * filter 执行失败返回
     *
     * @param exchange
     * @param status
     * @return
     */
    protected Mono<Void> responseFail(ServerWebExchange exchange, MyStatus status) {

        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(MyResponse.of(status, null));
        } catch (JsonProcessingException e) {
            log.warn("【AF061】json error: ", e);
        }

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(dataBuffer));
    }

}
