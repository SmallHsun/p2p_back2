package org.example.p2pcdn_backserver2.payload;

// 註冊消息類
public class RegisterMessage {
    private String clientId;
    private String type;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
// getters and setters
}