package org.example.p2pcdn_backserver2.service;


import org.example.p2pcdn_backserver2.model.Client;
import org.example.p2pcdn_backserver2.model.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PeerCacheService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long TTL = 6000; // 秒數

    private String buildKey(String hash) {
        return "peer:hash:" + hash;
    }

    public void cachePeers(String hash, List<Client> peers) {
        redisTemplate.opsForValue().set(buildKey(hash), peers, TTL, TimeUnit.SECONDS);
    }

    public List<Client> getCachedPeers(String hash) {
        Object val = redisTemplate.opsForValue().get(buildKey(hash));
        if (val instanceof List<?>) {
            return (List<Client>) val;
        }
        return null;
    }
    public Map<String, List<Client>> getBatchCachedPeers(List<String> hashes) {
        List<String> keys = hashes.stream().map(this::buildKey).toList();
        List<Object> cachedValues = redisTemplate.opsForValue().multiGet(keys);

        Map<String, List<Client>> result = new HashMap<>();
        for (int i = 0; i < hashes.size(); i++) {
            Object val = cachedValues.get(i);
            if (val instanceof List<?>) {
                result.put(hashes.get(i), (List<Client>) val);
            }
        }
        return result;
    }
}

