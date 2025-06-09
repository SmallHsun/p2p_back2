package org.example.p2pcdn_backserver2.repository;

import org.example.p2pcdn_backserver2.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClientRepository extends MongoRepository<Client,String> {
    List<Client> findByStatusEquals(String status);
}
