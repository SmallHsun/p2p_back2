package org.example.p2pcdn_backserver2.model;

import lombok.Data;

import java.util.List;

@Data
public class BatchContentLocationRequest {
    private List<String> contentHashes;

    public List<String> getContentHashes() {
        return contentHashes;
    }

    public void setContentHashes(List<String> contentHashes) {
        this.contentHashes = contentHashes;
    }
}
