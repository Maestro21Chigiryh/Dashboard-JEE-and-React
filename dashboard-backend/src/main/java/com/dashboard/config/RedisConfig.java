// package com.dashboard.config;

// import jakarta.annotation.PreDestroy;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.enterprise.inject.Produces;
// import jakarta.inject.Singleton;
// import redis.clients.jedis.JedisPool;
// import redis.clients.jedis.JedisPoolConfig;

// @ApplicationScoped
// public class RedisConfig {
    
//     private JedisPool jedisPool;
    
//     // Make this public so it can be called directly from ApplicationConfig
//     public void init() {
//         if (jedisPool == null) {
//             JedisPoolConfig poolConfig = new JedisPoolConfig();
//             poolConfig.setMaxTotal(10);
//             poolConfig.setMaxIdle(5);
//             poolConfig.setMinIdle(1);
            
//             jedisPool = new JedisPool(poolConfig, "localhost", 6379);
//         }
//     }
    
//     @Produces
//     @Singleton
//     public JedisPool getJedisPool() {
//         if (jedisPool == null) {
//             init();
//         }
//         return jedisPool;
//     }
    
//     @PreDestroy
//     public void close() {
//         if (jedisPool != null && !jedisPool.isClosed()) {
//             jedisPool.close();
//         }
//     }
// }

package com.dashboard.config;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;

@ApplicationScoped
public class RedisConfig {

    private JedisPool jedisPool;

    public void init() {
        if (jedisPool == null) {

            try {

                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(10);
                poolConfig.setMaxIdle(5);
                poolConfig.setMinIdle(1);

                String redisUrl = System.getenv("REDIS_URL");

                if (redisUrl == null || redisUrl.isEmpty()) {
                    throw new RuntimeException("REDIS_URL non définie");
                }

                URI uri = new URI(redisUrl);

                String host = uri.getHost();
                int port = uri.getPort();
                String password = uri.getUserInfo().split(":", 2)[1];

                boolean ssl = uri.getScheme().equals("rediss");

                jedisPool = new JedisPool(
                        poolConfig,
                        host,
                        port,
                        2000,
                        password,
                        ssl
                );

                System.out.println("✅ Redis connecté : " + host + ":" + port + " SSL=" + ssl);

                // test connexion
                try (var jedis = jedisPool.getResource()) {
                    jedis.set("redis_test", "ok");
                    System.out.println("Redis test: " + jedis.get("redis_test"));
                }

            } catch (Exception e) {
                System.err.println("❌ Redis init error: " + e.getMessage());
                e.printStackTrace();
            }
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