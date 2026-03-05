package com.dashboard.config;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class RedisConfig {
    
    private JedisPool jedisPool;
    
    // Make this public so it can be called directly from ApplicationConfig
    public void init() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(10);
            poolConfig.setMaxIdle(5);
            poolConfig.setMinIdle(1);
            
            jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        }
    }
    
    @Produces
    @Singleton
    public JedisPool getJedisPool() {
        if (jedisPool == null) {
            init();
        }
        return jedisPool;
    }
    
    @PreDestroy
    public void close() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}