package vip.yeee.memoo.common.websocket.netty.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memoo.common.websocket.netty.bootstrap.ServerEndpointBootstrap;

@ConditionalOnMissingBean(ServerEndpointBootstrap.class)
@Configuration
public class NettyWebSocketConfiguration {

    @Bean
    public ServerEndpointBootstrap serverEndpointExporter() {
        return new ServerEndpointBootstrap();
    }
}
