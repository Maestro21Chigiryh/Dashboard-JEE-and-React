// package com.dashboard.config;

// import org.bson.Document;

// import com.mongodb.client.MongoClient;
// import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoDatabase;

// import jakarta.annotation.PreDestroy;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.enterprise.inject.Produces;
// import jakarta.inject.Singleton;

// @ApplicationScoped
// public class MongoConfig {
    
//     private MongoClient mongoClient;
//     private MongoDatabase database;
//     private MongoCollection<Document> clientCollection;
//     // Add this to MongoConfig.java
//     private MongoCollection<Document> transactionCollection;
    
//     // Make this public so it can be called directly from ApplicationConfig
//     public void init() {
//         if (mongoClient == null) {
//             mongoClient = MongoClients.create("mongodb://localhost:27017");
//             database = mongoClient.getDatabase("dashboard");
//             clientCollection = database.getCollection("clients");
//             transactionCollection = database.getCollection("transactions");
//         }
//     }



//     @Produces
//     @Singleton
//     public MongoCollection<Document> getTransactionCollection() {
//         return transactionCollection;
//     }
    
//     @Produces
//     @Singleton
//     public MongoDatabase getDatabase() {
//         if (database == null) {
//             init();
//         }
//         return database;
//     }
    
//     @Produces
//     @Singleton
//     public MongoCollection<Document> getClientCollection() {
//         if (clientCollection == null) {
//             init();
//         }
//         return clientCollection;
//     }
    
//     @PreDestroy
//     public void close() {
//         if (mongoClient != null) {
//             mongoClient.close();
//         }
//     }
// }


package com.dashboard.config;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class MongoConfig {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> clientCollection;
    private MongoCollection<Document> transactionCollection;

    public void init() {
        if (mongoClient == null) {
            String uri = System.getenv("MONGO_URI");
            if (uri == null || uri.isEmpty()) {
                uri = "mongodb://localhost:27017";
            }

            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("dashboard");
            clientCollection = database.getCollection("clients");
            transactionCollection = database.getCollection("transactions");

            System.out.println("✅ MongoDB connected to: " + uri);

            // 🔹 Liste des collections existantes
            var collections = database.listCollectionNames().into(new ArrayList<>());
            System.out.println("Collections in 'dashboard': " + collections);

            // 🔹 Test simple : compter les clients
            long count = clientCollection.countDocuments();
            System.out.println("Number of documents in clients collection: " + count);
        }
    }

    @Produces
    @Singleton
    public MongoDatabase getDatabase() {
        if (database == null) {
            init();
        }
        return database;
    }

    @Produces
    @Singleton
    public MongoCollection<Document> getClientCollection() {
        if (clientCollection == null) {
            init();
        }
        return clientCollection;
    }

    @Produces
    @Singleton
    public MongoCollection<Document> getTransactionCollection() {
        if (transactionCollection == null) {
            init();
        }
        return transactionCollection;
    }

    @PreDestroy
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}