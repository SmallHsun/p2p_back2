package org.example.p2pcdn_backserver2.payload;

import lombok.Data;

@Data
public class ContentLocationRequest {

    private String contentHash;
    private double latitude;
    private double longitude;

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
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
}
