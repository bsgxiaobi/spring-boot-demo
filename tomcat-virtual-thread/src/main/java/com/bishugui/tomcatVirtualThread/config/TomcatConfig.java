package com.bishugui.tomcatVirtualThread.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * @author bi shugui
 * @description Tomcat配置
 * @date 2023/9/23 22:18
 */
@Configuration
public class TomcatConfig {

    TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer(){
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
