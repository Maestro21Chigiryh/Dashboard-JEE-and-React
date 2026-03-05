package com.dashboard.config;

import org.bson.Document;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.dashboard.repository.ClientRepository;
import com.dashboard.repository.RedisRepository;
import com.dashboard.repository.TransactionRepository;
import com.dashboard.service.AuthService;
import com.dashboard.service.ClientService;
import com.dashboard.service.TransactionService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.inject.Singleton;
import jakarta.ws.rs.ApplicationPath;
import redis.clients.jedis.JedisPool;

@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("com.dashboard");
        
        // Register our configs first so they're initialized
        MongoConfig mongoConfig = new MongoConfig();
        mongoConfig.init();
        
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.init();
        
        // Register configurations and services
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                // Bind MongoDB components
                bindAsContract(MongoConfig.class).in(Singleton.class).to(MongoConfig.class);
                bind(mongoConfig).to(MongoConfig.class);
                
                bind(mongoConfig.getDatabase()).to(MongoDatabase.class);
                bind(mongoConfig.getClientCollection()).to(new TypeLiteral<MongoCollection<Document>>() {});
                
                // Bind Redis components
                bindAsContract(RedisConfig.class).in(Singleton.class).to(RedisConfig.class);
                bind(redisConfig).to(RedisConfig.class);
                bind(redisConfig.getJedisPool()).to(JedisPool.class);
                
                // Register repositories
                bindAsContract(ClientRepository.class).in(Singleton.class);
                bindAsContract(RedisRepository.class).in(Singleton.class);
                
                // Register services
                bindAsContract(AuthService.class).in(Singleton.class);
                bindAsContract(ClientService.class).in(Singleton.class);

                // In ApplicationConfig.java, add to the configure() method:
                bindAsContract(TransactionRepository.class).in(Singleton.class);
                bindAsContract(TransactionService.class).in(Singleton.class);
            }
        });
    }
}