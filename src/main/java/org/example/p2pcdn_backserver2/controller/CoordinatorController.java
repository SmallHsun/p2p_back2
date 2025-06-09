package org.example.p2pcdn_backserver2.controller;

import org.example.p2pcdn_backserver2.model.BatchContentLocationRequest;
import org.example.p2pcdn_backserver2.model.Client;
import org.example.p2pcdn_backserver2.model.ClientInfo;
import org.example.p2pcdn_backserver2.model.ContentLocation;
import org.example.p2pcdn_backserver2.payload.ContentLocationRequest;
import org.example.p2pcdn_backserver2.service.CoordinatorService;
import org.example.p2pcdn_backserver2.service.MongoClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coordinator")
@CrossOrigin(origins = {"http://localhost:4173", "http://localhost:3000"}, allowCredentials = "true")

public class CoordinatorController {

    @Autowired
    private CoordinatorService coordinatorService;
    @Autowired
    private MongoClientService mongoClientService;

    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@RequestBody Client client) {
        coordinatorService.registerClient(client);
        System.out.println("Client registered: " + client.getClientId());
        return ResponseEntity.ok("Client registered successfully");
    }

    @PostMapping("/update-status")
    public ResponseEntity<String> updateClientStatus(@RequestBody Map<String, Object> request) {
        String clientId = (String) request.get("clientId");
        String status = (String) request.get("status");
        Long expireTime = request.get("expireTime") != null ?
                Long.valueOf(request.get("expireTime").toString()) : null;

        if (clientId != null && status != null) {
            // 更新狀態
            mongoClientService.updateClientStatus(clientId, status);

            // 如果提供了過期時間，也更新過期時間
            if (expireTime != null) {
                mongoClientService.updateClientExpireTime(clientId, expireTime);
            }

            return ResponseEntity.ok("Client status and expire time updated successfully");
        } else {
            return ResponseEntity.badRequest().body("clientId or status is missing");
        }
    }

    @PostMapping("/register-content")
    public ResponseEntity<String> registerContent(@RequestBody ContentLocation contentLocation) {
        List<ClientInfo> clientInfoList = contentLocation.getClientInfo();
        if (clientInfoList != null && !clientInfoList.isEmpty()) {
            String clientId = clientInfoList.get(0).getClientId();
            Double latitude = clientInfoList.get(0).getLatitude();
            Double longitude = clientInfoList.get(0).getLongitude();

            coordinatorService.registerContent(clientId, latitude, longitude, contentLocation.getContentHash());
            return ResponseEntity.ok("Content registered successfully");
        } else {
            return ResponseEntity.badRequest().body("clientInfo is missing or empty");
        }
    }

    @PostMapping("/remove-content")
    public ResponseEntity<String> removeContent(@RequestBody Map<String, String> request) {
        String clientId = request.get("clientId");
        String contentHash = request.get("contentHash");

        coordinatorService.removeContent(clientId, contentHash);
        return ResponseEntity.ok("Content removed successfully");
    }

    @PostMapping("/find-peers")
    public ResponseEntity<List<Client>> findPeersForContent(
            @RequestBody ContentLocationRequest contentLocationRequest) {
        System.out.println("Finding peers for content: " + contentLocationRequest.getContentHash());
        List<Client> peers = coordinatorService.findPeersForContent(contentLocationRequest.getContentHash());
        return ResponseEntity.ok(peers);
    }

    @PostMapping("/find-peers-batch")
    public ResponseEntity<Map<String, List<Client>>> findPeersBatch(
            @RequestBody BatchContentLocationRequest batchRequest) {
        // 限制批次大小，防止過載
        List<String> contentHashes = batchRequest.getContentHashes();
        if (contentHashes == null || contentHashes.isEmpty()) {
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
        // 限制最多100個hash
        if (contentHashes.size() > 100) {
            contentHashes = contentHashes.subList(0, 100);
        }

        Map<String, List<Client>> batchResults = coordinatorService.findPeersForContentBatch(contentHashes);

        return ResponseEntity.ok(batchResults);
    }

}