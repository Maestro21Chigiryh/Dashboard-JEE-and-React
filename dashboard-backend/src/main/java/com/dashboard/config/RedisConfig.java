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

@ApplicationScoped
public class RedisConfig {

    private JedisPool jedisPool;

    public void init() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(10);
            poolConfig.setMaxIdle(5);
            poolConfig.setMinIdle(1);

            String redisUrl = System.getenv("REDIS_URL"); // URL Upstash
            String redisHost = "localhost";
            int redisPort = 6379;
            String redisPassword = null;

            if (redisUrl != null && !redisUrl.isEmpty()) {
                try {
                    java.net.URI uri = new java.net.URI(redisUrl);
                    redisHost = uri.getHost();
                    redisPort = uri.getPort();
                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        redisPassword = userInfo.split(":", 2)[1];
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Redis URL invalide : " + e.getMessage());
                }
            }

            if (redisPassword != null) {
                jedisPool = new JedisPool(poolConfig, redisHost, redisPort, 2000, redisPassword);
            } else {
                jedisPool = new JedisPool(poolConfig, redisHost, redisPort);
            }

            System.out.println("✅ Redis initialized: " + redisHost + ":" + redisPort);

            // 🔹 Test simple
            try (var jedis = jedisPool.getResource()) {
                jedis.set("test_key", "ok");
                String value = jedis.get("test_key");
                System.out.println("Redis test_key value: " + value);
            } catch (Exception e) {
                System.err.println("❌ Redis test failed: " + e.getMessage());
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