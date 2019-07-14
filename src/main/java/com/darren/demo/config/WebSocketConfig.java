package com.darren.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //外部可订阅的socket数据服务(socket/topic为服务端广播消息的客户端订阅地址前缀，socket/user为客户端个性化消息的订阅地址前缀)
    private static final String[] brokers = {
            "/socket/topic","/socket/user"
    };


    //配置stomp端点，即客户端的握手连接机制
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")   //注册Stomp端点，websocket客户端订阅或发布消息前，需通过此端点(路径)先连接(握手)
                .setAllowedOrigins("*")            //允许跨域访问
                .withSockJS();                     //使用sockJS
    }

    // 消息代理(消息访问路径)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(brokers);// 配置客户端可订阅的消息前缀
        registry.setApplicationDestinationPrefixes("/socket");//前端个性化订阅前，用stomp协议发送消息到服务端的地址前缀
        registry.setUserDestinationPrefix("/socket/user");//推送消息到订阅用户的前缀地址(如前端订阅为"/user/message",则服务端send_to_user直接为"message")
    }
}
