package org.example.p2pcdn_backserver2.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "clients")
public class Client {
    @Id
    private String clientId;
    private double latitude;
    private double longitude;
    private Set<String> contentHashes = new HashSet<>();
    private String status;
    private long expireTime; // 新增：過期時間

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Set<String> getContentHashes() {
        return contentHashes;
    }

    public void setContentHashes(Set<String> contentHashes) {
        this.contentHashes = contentHashes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void addContentHash(String hash) {
        this.contentHashes.add(hash);
    }

    public void removeContentHash(String hash) {
        this.contentHashes.remove(hash);
    }
}