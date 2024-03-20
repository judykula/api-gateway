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

import com.jwy.api.gateway.util.StatusUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *     黑名单过滤器
 *
 *     目前阶段支持通过属性配置黑名单
 * </p>
 * <p>
 *     默认是开启状态
 *     某些情况下，可以使用防火墙/nginx来支持黑名单功能，
 *     可以配置{@code sys.filters.blacklist.enabled=false}来关闭黑名单功能
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/12
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "sys.filters.blacklist.enabled", matchIfMissing = true)
public class BlackListFilter extends AbstractFilter{

    /**黑名单存储在这里*/
    @Value("#{'${sys.blacklist:}'.split(',')}")
    private List<String> blackList;

    private static PathMatcher defaultPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Set<URI> uris = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        final String _uri = uris.stream().findFirst().get().getPath();

        for (String pattern : blackList) {
            if("/**".equals(pattern)) continue;//排除误写，导致所有请求被ban的可能
            boolean match = defaultPathMatcher.match(pattern, _uri);
            if(match){
                log.debug("【BLF062】in blacklist uri = {}", _uri);
                return super.responseFail(exchange, StatusUtils.BlacklistMatherError);
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return ORDER_BLACKLIST;
    }

}
