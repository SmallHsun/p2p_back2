package org.example.p2pcdn_backserver2.controller;

import org.example.p2pcdn_backserver2.model.SignalMessage;
import org.example.p2pcdn_backserver2.payload.RegisterMessage;
import org.example.p2pcdn_backserver2.service.MongoClientService;
import org.example.p2pcdn_backserver2.utils.WebSocketConnectionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = {"http://localhost:4173","https://b8ac-2001-b011-b803-16e4-7d91-cc02-b804-da82.ngrok-free.app/"},allowCredentials = "true")


public class SignalController {

    private final SimpMessagingTemplate template;
    private final MongoClientService  mongoClientService;
    @Autowired
    private  WebSocketConnectionTracker connectionTracker;

    public SignalController(SimpMessagingTemplate messagingTemplate, MongoClientService mongoClientService) {
        this.template = messagingTemplate;
        this.mongoClientService = mongoClientService;
    }


    @MessageMapping("/register")
            public void registerClient(RegisterMessage message, SimpMessageHeaderAccessor headerAccessor) {
                String clientId = message.getClientId();
                if (clientId != null) {
                    connectionTracker.recordConnection();
                    int count = connectionTracker.getRecentConnectionCount();
                    System.out.println("3 秒內 WebSocket 註冊數: " + count);
                    // 將 clientId 保存到 WebSocket 會話
                    headerAccessor.getSessionAttributes().put("CLIENT_ID", clientId);

                }else {
        }
    }
    @MessageMapping("/signal") // 對應前端的 /app/signal
    public void signal(@Payload SignalMessage message) {

        System.out.println("收到來自"+message.getSender()+"的消息: "+message.getType());
        // 發送到指定用戶的 queue
        template.convertAndSend("/topic/signal."+message.getReceiver(), message);

    }
}
