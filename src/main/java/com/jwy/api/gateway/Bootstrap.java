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
package com.jwy.api.gateway;

import com.jwy.medusa.MyCoreAutoConfiguration;
import com.jwy.medusa.consul.MyConsulConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * <p>
 *     系统启动类
 * </p>
 *
 * @author Jiang Wanyu
 * @version 1.0
 * @date 2024/3/7
 */
@Import(MyConsulConfiguration.class)
@SpringBootApplication(exclude = {MyCoreAutoConfiguration.class})
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
