package org.example.p2pcdn_backserver2.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Data

public class ClientInfo {


    private String clientId;
    private double latitude;
    private double longitude;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientInfo)) return false;
        ClientInfo that = (ClientInfo) o;
        return Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }
}
