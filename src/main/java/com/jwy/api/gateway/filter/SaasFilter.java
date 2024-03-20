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

import com.jwy.api.gateway.processor.SaasProcessor;
import com.jwy.api.gateway.util.StatusUtils;
import com.jwy.medusa.common.utils.MyHttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 *     saas支持
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/19
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "sys.filters.saas.enabled", matchIfMissing = true)
public class SaasFilter extends AbstractFilter{

    @Autowired
    private SaasProcessor saasProcessor;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest httpRequest = exchange.getRequest();
        String tenant = httpRequest.getHeaders().getFirst(MyHttpHeaders.REQUEST_SAAS_TENANT);

        //如果请求已经携带租户(tenant)标识，则无需处理
        if(StringUtils.isNotEmpty(tenant)){
            return chain.filter(exchange);
        }

        //如果请求没有租户(tenant)信息，则需要根据其host进行匹配
        String host = httpRequest.getHeaders().getFirst("Host");
        log.debug("【SF060】host: {}", host);
        if(StringUtils.isEmpty(host)){
            return super.responseFail(exchange, StatusUtils.SaasNoTenantError);
        }

        //如果hostTenant cache是有值的，说明tenant-center是起作用的，需要进行host->tenant转换
        Map<String, String> hostTenant = this.saasProcessor.getHostAndTenantMap();
        if(MapUtils.isNotEmpty(hostTenant)){
            String _tenant = hostTenant.get(host);

            if(StringUtils.isEmpty(_tenant))
                return chain.filter(exchange);

            ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
                    .headers(httpHeaders -> {
                        httpHeaders.set(MyHttpHeaders.REQUEST_SAAS_TENANT, _tenant);
                    })
                    .build();
            return chain.filter(exchange.mutate().request(mutateRequest).build());
        }

        //通过saas filter的三种办法：
        //1. 不启用saas支持：sys.filters.saas.enabled=false
        //2. header设置X-MY-TENANT
        //3. 在tenant-center添加host-tenant对应匹配
        return super.responseFail(exchange, StatusUtils.SaasNoTenantError);
    }

    @Override
    public int getOrder() {
        return ORDER_SAAS;
    }
}
