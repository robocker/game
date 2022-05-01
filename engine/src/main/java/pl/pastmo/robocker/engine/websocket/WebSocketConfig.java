package pl.pastmo.robocker.engine.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import pl.pastmo.robocker.engine.service.MessageService;

@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer{//implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private MessageService messageService;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageService, "/state").setAllowedOrigins("*");
    }

}