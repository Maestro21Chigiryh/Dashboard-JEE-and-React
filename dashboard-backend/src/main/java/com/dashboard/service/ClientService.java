package com.dashboard.service;

import java.util.List;

import com.dashboard.model.Client;
import com.dashboard.repository.ClientRepository;
import com.dashboard.repository.RedisRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClientService {
    
    private final ClientRepository clientRepository;
    private final RedisRepository redisRepository;
    
    @Inject
    public ClientService(ClientRepository clientRepository, RedisRepository redisRepository) {
        this.clientRepository = clientRepository;
        this.redisRepository = redisRepository;
    }
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    public List<Client> getTopClients(int limit) {
        boolean hasCache = redisRepository.hasTopClientsCache();
        
        if (hasCache) {
            List<Client> cachedClients = redisRepository.getCachedTopClients();
            if (!cachedClients.isEmpty() && cachedClients.size() >= limit) {
                return cachedClients.subList(0, limit);
            }
        }
        
        List<Client> topClients = clientRepository.findTopClients(limit);
        redisRepository.cacheTopClients(topClients);
        
        return topClients;
    }
    
    public void generateClients(int count) {
        long existingCount = clientRepository.countClients();
        
        if (existingCount == 0) {
            clientRepository.generateRandomClients(count);
            redisRepository.clearCache();
        }
    }
    
    public long getClientCount() {
        return clientRepository.countClients();
    }
    
    public void resetClients() {
        clientRepository.clearAll();
        redisRepository.clearCache();
    }
}