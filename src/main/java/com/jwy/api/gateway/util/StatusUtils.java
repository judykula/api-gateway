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
package com.jwy.api.gateway.util;

import com.jwy.medusa.mvc.MyStatus;
import com.jwy.medusa.mvc.MyStatusz;

/**
 * <p>
 *     网关状态码定义
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/18
 */
public class StatusUtils extends MyStatusz {

    /**黑名单不准访问*/
    public static MyStatus BlacklistMatherError =  MyStatus.of(10010, "[banned] access not allowed");

    /**saas请求未认证，没有有效的tenant信息*/
    public static MyStatus SaasNoTenantError =  MyStatus.of(10020, "Illegal access: no tenant identification information");

}
