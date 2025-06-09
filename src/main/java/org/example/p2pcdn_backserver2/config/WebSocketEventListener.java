package org.example.p2pcdn_backserver2.config;

import org.example.p2pcdn_backserver2.model.Client;
import org.example.p2pcdn_backserver2.service.CoordinatorService;
import org.example.p2pcdn_backserver2.service.MongoClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Component
public class WebSocketEventListener {

//    @Autowired
//    private SignalingService signalingService;
    @Autowired
    private MongoClientService mongoClientService;
    @Autowired
    private CoordinatorService coordinatorService;

    /**
     * 處理WebSocket連接事件
     * @param event 連接事件
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        System.out.println("WebSocket 會話已連接: " + sessionId);
    }

    /**
     * 處理WebSocket斷開連接事件
     * @param event 斷開連接事件
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        // 獲取用戶ID
        String clientId = (String) headerAccessor.getSessionAttributes().get("CLIENT_ID");
        mongoClientService.updateClientStatus(clientId, "OFFLINE");
        System.out.println("WebSocket 會話已斷開: " + sessionId);

    }


//    @Scheduled(fixedRate = 3600000) // 每小時執行一次
//    public void cleanExpiredClients() {
//        long now = System.currentTimeMillis();
//        List<Client> clients = mongoClientService.getAllClients();
//
//        for (Client client : clients) {
//            if (client.getExpireTime() < now) {
//                System.out.println("清理過期客戶端: " + client.getClientId());
//
//                // 刪除客戶端的內容
//                for (String contentHash : client.getContentHashes()) {
//                    coordinatorService.removeContent(client.getClientId(), contentHash);
//                }
//
//                // 刪除客戶端記錄
//                mongoClientService.deleteClient(client.getClientId());
//            }
//        }
//    }
}
