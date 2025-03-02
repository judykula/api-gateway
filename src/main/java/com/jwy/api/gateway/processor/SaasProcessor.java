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
package com.jwy.api.gateway.processor;

import com.google.common.collect.Maps;
import com.jwy.api.gateway.client.TenantCenterClient;
import com.jwy.medusa.mvc.MyResponse;
import com.jwy.wisp.pojo.response.saas.TenantHostVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *     处理saas逻辑，加载&缓存tenant信息
 * </p>
 * <p>
 *     可以配置{@code sys.saas.refresh.enabled=false}来关闭此组件
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/19
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "sys.saas.refresh.enabled", matchIfMissing = true)
public class SaasProcessor {

    /**上一次执行成功的时间*/
    private long preExecuteSuccessTime = 0;
    /**上一次任务执行失败的时间*/
    private long preTaskErrorTime = 0;
    /**存储 host-tenant 对应关系*/
    private final Map<String, String> hostAndTenantMap = Maps.newHashMap();

    @Autowired
    private TenantCenterClient tenantCenterClient;

    /**
     * 获取 host - tenant对应关系
     *
     * @return
     */
    public Map<String, String> getHostAndTenantMap() {
        synchronized (hostAndTenantMap){
            return hostAndTenantMap;
        }
    }

    @Scheduled(fixedDelay = 1_000)
    public void refresh() {

        Mono<MyResponse<Long>> resultMono = this.tenantCenterClient.getLatestUpdateTs();
        resultMono.doOnSuccess(response -> {

            long ts = response.getData();
            if(ts - preExecuteSuccessTime < 1000){
                //log.warn("【SP061】ignore with latest time: {}", ts);
                return;
            }
            //如果不行的话，可以试试 filter()-> then()
            doUpdate().subscribe();
        }).doOnError(throwable -> {
            if(System.currentTimeMillis() - preTaskErrorTime >= 5000){
                log.warn("【SP081】getLatestUpdateTs fail", throwable);
                preTaskErrorTime = System.currentTimeMillis();
            }
        }).subscribe();
    }

    /**
     * 执行更新操作
     *
     * @return
     */
    private Mono<Void> doUpdate(){

        Mono<MyResponse<List<TenantHostVo>>> resultMono = tenantCenterClient.getTenantHosts();
        return resultMono.doOnSuccess(response -> {

            List<TenantHostVo> ths = response.getData();
            if(CollectionUtils.isEmpty(ths)) return;

            Map<String, String> map = ths.stream().collect(Collectors.toMap(tenantHost -> tenantHost.getHost(), tenantHost -> tenantHost.getTenant()));
            synchronized (hostAndTenantMap){
                hostAndTenantMap.clear();
                hostAndTenantMap.putAll(map);
            }
            preExecuteSuccessTime = System.currentTimeMillis();
            log.info("【SP097】preExecuteTime set to: {}", preExecuteSuccessTime);
        }).doOnError(throwable -> {
            log.warn("【SP098】getTenantHosts fail", throwable);
        }).then();
    }



}
