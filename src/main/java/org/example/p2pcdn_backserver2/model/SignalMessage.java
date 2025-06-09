package org.example.p2pcdn_backserver2.model;

import lombok.Data;

@Data
public class SignalMessage {
    private String sender;
    private String receiver;
    private String type; // offer, answer, candidate
    private Object data;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    // getters & setters
}