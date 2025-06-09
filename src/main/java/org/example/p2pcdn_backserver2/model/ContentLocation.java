package org.example.p2pcdn_backserver2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "content_locations")
public class ContentLocation {

    @Id
    private String contentHash;
    private List<org.example.p2pcdn_backserver2.model.ClientInfo> clientInfo;

    public ContentLocation(String contentHash, List<org.example.p2pcdn_backserver2.model.ClientInfo> clientInfo) {
        this.contentHash = contentHash;
        this.clientInfo = clientInfo;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

//    public String getContentUrl() {
//        return contentUrl;
//    }
//
//    public void setContentUrl(String contentUrl) {
//        this.contentUrl = contentUrl;
//    }

    public List<org.example.p2pcdn_backserver2.model.ClientInfo> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(List<org.example.p2pcdn_backserver2.model.ClientInfo> clientInfo) {
        this.clientInfo = clientInfo;
    }


    public void removeClientId(String clientId) {
        this.clientInfo.removeIf(info -> info.getClientId().equals(clientId));
    }
}