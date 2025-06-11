package org.example.p2pcdn_backserver2.utils;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class WebSocketConnectionTracker {

    private final ConcurrentLinkedQueue<Long> recentConnections = new ConcurrentLinkedQueue<>();
    private final long TIME_WINDOW_MS = 2000; // 3 秒

    public void recordConnection() {
        long now = System.currentTimeMillis();
        recentConnections.add(now);

        // 移除超過 3 秒前的連線紀錄
        while (!recentConnections.isEmpty() && now - recentConnections.peek() > TIME_WINDOW_MS) {
            recentConnections.poll();
        }
    }

    public int getRecentConnectionCount() {
        long now = System.currentTimeMillis();

        // 確保資料是最新的
        while (!recentConnections.isEmpty() && now - recentConnections.peek() > TIME_WINDOW_MS) {
            recentConnections.poll();
        }

        return recentConnections.size();
    }
}