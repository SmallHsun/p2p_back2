package org.example.p2pcdn_backserver2.service;

import org.example.p2pcdn_backserver2.model.Client;
import org.example.p2pcdn_backserver2.model.ClientInfo;
import org.example.p2pcdn_backserver2.model.ContentLocation;
import org.example.p2pcdn_backserver2.repository.ContentLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoordinatorService {


    private static final Logger log = LoggerFactory.getLogger(CoordinatorService.class);
    @Autowired
    private org.example.p2pcdn_backserver2.service.MongoClientService mongoClientService;
    @Autowired
    private ContentLocationRepository contentLocationRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PeerCacheService cacheService;

    public void registerClient(Client client) {
        // 確保設置過期時間
        if (client.getExpireTime() == 0) {
            // 如果前端沒有提供過期時間，設置默認值（例如7天後）
            client.setExpireTime(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        }
        mongoClientService.saveClient(client);
    }

    public void registerContent(String clientId, Double longitude, Double latitude, String contentHash) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setClientId(clientId);
        clientInfo.setLongitude(longitude);
        clientInfo.setLatitude(latitude);
        // 更新Redis中的客戶端資訊
//        mongoClientService.addContentToClient(clientId, contentHash);
        Query query = Query.query(Criteria.where("_id").is(contentHash));
        Update update = new Update().addToSet("clientInfo", clientInfo);

        mongoTemplate.upsert(query, update, ContentLocation.class);
        Client client = mongoClientService.getClientById(clientId);
        if (client != null && "ONLINE".equals(client.getStatus())) {
            String key = contentHash;
            List<Client> cached = cacheService.getCachedPeers(key);
            if (cached != null) {
                cached.add(client); // 可加排重邏輯
                cacheService.cachePeers(key, cached); // 更新快取
            } else {
                cacheService.cachePeers(key, List.of(client));
            }
        }
    }

    public void removeContent(String clientId, String contentHash) {
        // 更新Redis中的客戶端資訊
        mongoClientService.removeContentFromClient(clientId, contentHash);

        // 更新MongoDB中的內容位置資訊
        Optional<ContentLocation> contentLocationOpt = contentLocationRepository.findById(contentHash);
        if (contentLocationOpt.isPresent()) {
            ContentLocation contentLocation = contentLocationOpt.get();
            contentLocation.removeClientId(clientId);

            // 如果 clientInfo 已空，刪除整筆資料；否則更新
            if (contentLocation.getClientInfo().isEmpty()) {
                contentLocationRepository.delete(contentLocation);
            } else {
                contentLocationRepository.save(contentLocation);
            }
        }
    }

    public List<Client> findPeersForContent(String contentHash) {
        List<Client> nearbyPeers = new ArrayList<>();

        Optional<ContentLocation> contentLocationOpt = contentLocationRepository.findById(contentHash);
        if (contentLocationOpt.isPresent()) {
            List<ClientInfo> clientInfos = contentLocationOpt.get().getClientInfo();

            for (ClientInfo info : clientInfos) {
                Client client = new Client();
                client.setClientId(info.getClientId());
                nearbyPeers.add(client);
            }
        }

        return nearbyPeers;
    }


    //添加批次查詢方法
    public Map<String, List<Client>> findPeersForContentBatch(List<String> contentHashes) {
        Map<String, List<Client>> results = new HashMap<>();

        // 1. 先查 Redis 快取
        Map<String, List<Client>> cached = cacheService.getBatchCachedPeers(contentHashes);
        results.putAll(cached);

        // 加入 log：印出有命中的快取項目
        if (!cached.isEmpty()) {
            log.info("Cache hit for hashes: {}", cached.keySet().size());
        }

        // 2. 判斷有哪些 hash 沒命中 Redis
        List<String> missedHashes = contentHashes.stream()
                .filter(hash -> !cached.containsKey(hash))
                .toList();

        if (!missedHashes.isEmpty()) {
            log.info("Cache miss for hashes: {}", missedHashes);

            // 3. 查 Mongo（僅查未命中者）
            List<ContentLocation> contentLocations = contentLocationRepository.findClientIdsOnly(missedHashes);
            for (ContentLocation contentLocation : contentLocations) {
                List<Client> peers = new ArrayList<>();
                for (ClientInfo info : contentLocation.getClientInfo()) {
                    Client client = mongoClientService.getClientById(info.getClientId());
                    if (client != null && "ONLINE".equals(client.getStatus())) {
                        Client peerClient = new Client();
                        peerClient.setClientId(info.getClientId());
                        peers.add(peerClient);
                    }
                }

                results.put(contentLocation.getContentHash(), peers);
                cacheService.cachePeers(contentLocation.getContentHash(), peers);
            }

            // 4. 剩下找不到的也補空清單
            for (String hash : missedHashes) {
                if (!results.containsKey(hash)) {
                    results.put(hash, new ArrayList<>());
                    cacheService.cachePeers(hash, new ArrayList<>());
                }
            }
        }

        return results;
    }


}



