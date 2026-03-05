package com.dashboard.repository;

import java.util.ArrayList;
import java.util.List;

import com.dashboard.model.Client;
import com.google.gson.Gson;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@ApplicationScoped
public class RedisRepository {
    
    private JedisPool jedisPool;
    
    private final Gson gson = new Gson();
    private static final String TOP_CLIENTS_KEY = "top_clients";
    private static final int CACHE_EXPIRY_SECONDS = 3600; // 1 hour
    
    public RedisRepository() {
        // Default constructor needed for CDI
    }
    
    @Inject
    public RedisRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    public void cacheTopClients(List<Client> clients) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> serializedClients = new ArrayList<>();
            
            for (Client client : clients) {
                String jsonClient = gson.toJson(client);
                serializedClients.add(jsonClient);
            }
            
            String[] clientsArray = serializedClients.toArray(new String[0]);
            
            jedis.del(TOP_CLIENTS_KEY);
            if (clientsArray.length > 0) {
                jedis.rpush(TOP_CLIENTS_KEY, clientsArray);
                jedis.expire(TOP_CLIENTS_KEY, CACHE_EXPIRY_SECONDS);
            }
        }
    }
    
    public List<Client> getCachedTopClients() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> cachedClients = jedis.lrange(TOP_CLIENTS_KEY, 0, -1);
            List<Client> clients = new ArrayList<>();
            
            for (String jsonClient : cachedClients) {
                Client client = gson.fromJson(jsonClient, Client.class);
                clients.add(client);
            }
            
            return clients;
        }
    }
    
    public boolean hasTopClientsCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(TOP_CLIENTS_KEY);
        }
    }
    
    public void clearCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(TOP_CLIENTS_KEY);
        }
    }
}