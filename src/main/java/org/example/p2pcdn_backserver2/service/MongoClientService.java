package org.example.p2pcdn_backserver2.service;

import org.example.p2pcdn_backserver2.exception.ClientNotFoundException;
import org.example.p2pcdn_backserver2.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.p2pcdn_backserver2.model.Client;

import java.util.List;
import java.util.Optional;

@Service
public class MongoClientService {
    @Autowired
    private ClientRepository clientRepository;

    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    public Optional<Client> getClient(String clientId) {
        return clientRepository.findById(clientId);
    }

    public void updateClientStatus(String clientId, String status) {
//        clientRepository.findById(clientId).ifPresent(client -> {
//            client.setStatus(status);
//            clientRepository.save(client);
//        });
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("找不到 ID 為 " + clientId + " 的 Client"));

        client.setStatus(status);
        clientRepository.save(client);
    }

    public void addContentToClient(String clientId, String contentHash) {
//        clientRepository.findById(clientId).ifPresent(client -> {
//            client.addContentHash(contentHash);
//            clientRepository.save(client);
//        });
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("找不到 ID 為 " + clientId + " 的 Client"));
        client.addContentHash(contentHash);
        clientRepository.save(client);

    }

    public void removeContentFromClient(String clientId, String contentHash) {
//        clientRepository.findById(clientId).ifPresent(client -> {
//            client.removeContentHash(contentHash);
//            clientRepository.save(client);
//        });
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("找不到 ID 為 " + clientId + " 的 Client"));
        client.removeContentHash(contentHash);
        clientRepository.save(client);
    }
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public void deleteClient(String clientId) {
        clientRepository.deleteById(clientId);
    }

    public void updateClientExpireTime(String clientId, long expireTime) {
        clientRepository.findById(clientId).ifPresent(client -> {
            client.setExpireTime(expireTime);
            clientRepository.save(client);
        });
    }

    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("找不到 ID 為 " + clientId + " 的 Client"));
    }
}
