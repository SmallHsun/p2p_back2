package org.example.p2pcdn_backserver2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") //
                .setAllowedOriginPatterns("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
        // 配置 RabbitMQ 為外部 broker
        registry.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("admin")
                .setClientPasscode("supersecret")
                .setSystemLogin("admin")
                .setSystemPasscode("supersecret")
                .setSystemHeartbeatReceiveInterval(4000)
                .setSystemHeartbeatSendInterval(5000);

    }
}