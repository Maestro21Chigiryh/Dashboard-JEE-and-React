// TransactionRepository.java
package com.dashboard.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.dashboard.model.Transaction;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionRepository {
    
    private MongoCollection<Document> transactionCollection;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public TransactionRepository() {
        // Default constructor needed for CDI
    }
    
    @Inject
    public TransactionRepository(MongoCollection<Document> transactionCollection) {
        this.transactionCollection = transactionCollection;
    }
    
    public List<Transaction> findByClientId(ObjectId clientId) {
        List<Transaction> transactions = new ArrayList<>();
        
        transactionCollection.find(Filters.eq("clientId", clientId))
                .sort(Sorts.ascending("date"))
                .forEach(document -> {
                    Transaction transaction = documentToTransaction(document);
                    transactions.add(transaction);
                });
        
        return transactions;
    }
    
    public List<Transaction> findTransactionsByClientIds(List<ObjectId> clientIds) {
        List<Transaction> transactions = new ArrayList<>();
        
        transactionCollection.find(Filters.in("clientId", clientIds))
                .forEach(document -> {
                    Transaction transaction = documentToTransaction(document);
                    transactions.add(transaction);
                });
        
        return transactions;
    }
    
    public void generateTransactionsForClient(ObjectId clientId, int purchaseCount, LocalDate firstDate, LocalDate lastDate) {
        List<Document> transactions = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        // Generate a range of dates between first and last purchase
        long daysBetween = firstDate.until(lastDate).getDays() + 1;
        List<LocalDate> transactionDates = new ArrayList<>();
        
        // Generate random dates
        for (int i = 0; i < purchaseCount; i++) {
            if (daysBetween > 1) {
                long randomDay = random.nextLong(daysBetween);
                transactionDates.add(firstDate.plusDays(randomDay));
            } else {
                transactionDates.add(firstDate);
            }
        }
        
        // Sort dates chronologically
        transactionDates.sort(LocalDate::compareTo);
        
        // Generate transactions for each date
        for (LocalDate date : transactionDates) {
            // Random transaction amount between 10 and 1000
            double amount = Math.round(random.nextDouble(10, 1000) * 100) / 100.0;
            int quantity = random.nextInt(1, 10);
            String productName = getRandomProductName();
            
            Document transaction = new Document();
            transaction.put("_id", new ObjectId());
            transaction.put("clientId", clientId);
            transaction.put("amount", amount);
            transaction.put("date", date.format(DATE_FORMATTER));
            transaction.put("productName", productName);
            transaction.put("quantity", quantity);
            
            transactions.add(transaction);
        }
        
        if (!transactions.isEmpty()) {
            transactionCollection.insertMany(transactions);
        }
    }
    
    private String getRandomProductName() {
        String[] products = {
            "Laptop", "Smartphone", "Tablet", "Headphones", "Monitor", 
            "Keyboard", "Mouse", "Printer", "Scanner", "Camera",
            "TV", "Speaker", "Microphone", "Game Console", "Router"
        };
        
        return products[ThreadLocalRandom.current().nextInt(products.length)];
    }
    
    private Transaction documentToTransaction(Document document) {
        return Transaction.builder()
                .id(document.getObjectId("_id"))
                .clientId(document.getObjectId("clientId"))
                .amount(document.getDouble("amount"))
                .date(document.getString("date"))
                .productName(document.getString("productName"))
                .quantity(document.getInteger("quantity"))
                .build();
    }
    
    public void clearAll() {
        transactionCollection.deleteMany(new Document());
    }
}