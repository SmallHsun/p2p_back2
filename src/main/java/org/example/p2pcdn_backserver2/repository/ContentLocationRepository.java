package org.example.p2pcdn_backserver2.repository;

import org.example.p2pcdn_backserver2.model.ContentLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ContentLocationRepository extends MongoRepository<ContentLocation, String> {
    // 查詢方法會自動由Spring Data實現

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'clientInfo.clientId': 1 }")
    List<ContentLocation> findClientIdsOnly(List<String> contentHashes);
}
