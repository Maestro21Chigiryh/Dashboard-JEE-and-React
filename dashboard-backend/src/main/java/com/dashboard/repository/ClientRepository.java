package com.dashboard.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.dashboard.model.Client;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClientRepository {
    
    private MongoCollection<Document> clientCollection;
    
    private final Gson gson = new Gson();
    
    public ClientRepository() {
        // Default constructor needed for CDI
    }
    
    @Inject
    public ClientRepository(MongoCollection<Document> clientCollection) {
        this.clientCollection = clientCollection;
    }
    
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        
        clientCollection.find().forEach(document -> {
            Client client = documentToClient(document);
            clients.add(client);
        });
        
        return clients;
    }
    
    public List<Client> findTopClients(int limit) {
        List<Client> topClients = new ArrayList<>();
        
        clientCollection.find()
                .sort(Sorts.descending("purchaseAmount"))
                .limit(limit)
                .forEach(document -> {
                    Client client = documentToClient(document);
                    topClients.add(client);
                });
        
        return topClients;
    }
    // Add this to ClientRepository.java
    @Inject
    private TransactionRepository transactionRepository;

    public void generateRandomClients(int count) {
        List<Document> documents = new ArrayList<>();
        int batchSize = 1000;
        
        for (int i = 0; i < count; i++) {
            Client client = generateRandomClient();
            Document document = clientToDocument(client);
            documents.add(document);
            
            // Insert in batches of 1000 to improve performance
            if (documents.size() >= batchSize || i == count - 1) {
                if (!documents.isEmpty()) {
                    clientCollection.insertMany(documents);
                    
                    // Now generate transactions for each client
                    for (Document doc : documents) {
                        Client newClient = documentToClient(doc);
                        LocalDate regDate = LocalDate.parse(newClient.getRegistrationDate());
                        LocalDate lastPurchaseDate = LocalDate.parse(newClient.getLastPurchaseDate());
                        
                        transactionRepository.generateTransactionsForClient(
                            newClient.getId(), 
                            newClient.getPurchaseCount(),
                            regDate,
                            lastPurchaseDate
                        );
                    }
                    
                    documents.clear();
                }
            }
        }
    }
    
    public long countClients() {
        return clientCollection.countDocuments();
    }
    
    public void clearAll() {
        clientCollection.deleteMany(new Document());
        transactionRepository.clearAll();
    }
    
    private Faker faker = new Faker(new Locale("fr"));

    private Client generateRandomClient() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String phone = faker.phoneNumber().phoneNumber();
        String address = faker.address().fullAddress();
        
        double purchaseAmount = Math.round(random.nextDouble(100, 10000) * 100) / 100.0;
        int purchaseCount = random.nextInt(1, 50);
        
        LocalDate now = LocalDate.now();
        LocalDate registrationDate = now.minusDays(random.nextInt(1, 1000));
        
        // Correction ici : s'assurer que la période est d'au moins 1 jour
        long daysUntilNow = registrationDate.until(now).getDays();
        LocalDate lastPurchaseDate;
        
        if (daysUntilNow > 0) {
            // S'il y a au moins un jour d'écart, générer une date aléatoire entre registrationDate et now
            lastPurchaseDate = registrationDate.plusDays(random.nextInt(1, (int)daysUntilNow + 1));
        } else {
            // Si registrationDate est aujourd'hui, définir lastPurchaseDate au même jour
            lastPurchaseDate = registrationDate;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return Client.builder()
                .id(new ObjectId())
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .purchaseAmount(purchaseAmount)
                .purchaseCount(purchaseCount)
                .registrationDate(registrationDate.format(formatter))
                .lastPurchaseDate(lastPurchaseDate.format(formatter))
                .active(random.nextBoolean())
                .build();
    }
    
    private Document clientToDocument(Client client) {
        Document document = new Document();
        document.put("_id", client.getId());
        document.put("name", client.getName());
        document.put("email", client.getEmail());
        document.put("phone", client.getPhone());
        document.put("address", client.getAddress());
        document.put("purchaseAmount", client.getPurchaseAmount());
        document.put("purchaseCount", client.getPurchaseCount());
        document.put("registrationDate", client.getRegistrationDate());
        document.put("lastPurchaseDate", client.getLastPurchaseDate());
        document.put("active", client.isActive());
        return document;
    }
    

    private Client documentToClient(Document document) {
        // Handle potential type mismatch and null values
        Object purchaseAmountObj = document.get("purchaseAmount");
        double purchaseAmount = 0.0;
        if (purchaseAmountObj != null) {
            if (purchaseAmountObj instanceof Integer) {
                purchaseAmount = ((Integer) purchaseAmountObj).doubleValue();
            } else if (purchaseAmountObj instanceof Double) {
                purchaseAmount = (Double) purchaseAmountObj;
            }
        }
        
        // Similar handling for other fields that might be missing
        Integer purchaseCount = document.getInteger("purchaseCount", 0);
        Boolean active = document.getBoolean("active", false);
        
        // Convertir l'ObjectId en chaîne directement
        ObjectId objectId = document.getObjectId("_id");
        String idStr = objectId != null ? objectId.toString() : null;
        
        return Client.builder()
                .id(objectId)  // Garder l'ObjectId pour les opérations internes
                .idStr(idStr)  // Ajouter un nouveau champ pour la chaîne
                .name(document.getString("name"))
                .email(document.getString("email"))
                .phone(document.getString("phone"))
                .address(document.getString("address"))
                .purchaseAmount(purchaseAmount)
                .purchaseCount(purchaseCount)
                .registrationDate(document.getString("registrationDate"))
                .lastPurchaseDate(document.getString("lastPurchaseDate"))
                .active(active)
                .build();
    }
}