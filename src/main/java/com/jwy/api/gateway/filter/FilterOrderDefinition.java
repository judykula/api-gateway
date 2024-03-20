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

/**
 * <p>
 *     定义Filter的执行顺序
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/12
 */
public interface FilterOrderDefinition {

    /**黑名单的优先级 {@link BlackListFilter}*/
    int ORDER_BLACKLIST = 10;

    /**Saas优先级 {@link SaasFilter}*/
    int ORDER_SAAS = 15;

}
