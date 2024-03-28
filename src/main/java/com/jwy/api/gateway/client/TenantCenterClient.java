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
package com.jwy.api.gateway.client;

import com.jwy.medusa.mvc.MyResponse;
import com.jwy.wisp.pojo.response.saas.TenantHostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 *     请求tenant-center的client
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/19
 */
@Component
public class TenantCenterClient {

    private final String Url_Latest_Update_Timestamp = "http://tenant-center/feign/site/updateTs";
    private final String Url_Brand_Hosts = "http://tenant-center/feign/site/hostAndTenants";

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * 获取最近更新的时间戳
     *
     * @return
     */
    public Mono<MyResponse<Long>> getLatestUpdateTs() {
        return this.webClientBuilder.build()
                .get().uri(Url_Latest_Update_Timestamp)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(ParameterizedTypeReference.forType(Long.class));
    }

    /**
     * get tenant-host pair lists
     *
     * @return
     * @throws Exception
     */
    public Mono<MyResponse<List<TenantHostVo>>> getTenantHosts() {

        return this.webClientBuilder.build()
                .get().uri(Url_Brand_Hosts)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(ParameterizedTypeReference.forType(List.class));
                //.toEntity(ParameterizedTypeReference.forType(List.class));
    }

}
