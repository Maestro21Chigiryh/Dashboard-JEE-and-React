// TransactionService.java - Ajout de journalisation pour mieux suivre les problèmes
package com.dashboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.dashboard.model.Client;
import com.dashboard.model.Transaction;
import com.dashboard.repository.ClientRepository;
import com.dashboard.repository.TransactionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionService {
    
    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());
    
    @Inject
    private TransactionRepository transactionRepository;
    
    @Inject
    private ClientRepository clientRepository;
    
    public List<Transaction> getClientTransactions(ObjectId clientId) {
        logger.info("Getting transactions for client ID: " + clientId.toString());
        return transactionRepository.findByClientId(clientId);
    }
    
    public Map<String, Object> getTopClientsTransactions(int limit) {
        List<Client> topClients = clientRepository.findTopClients(limit);
        List<ObjectId> clientIds = topClients.stream()
                                            .map(Client::getId)
                                            .collect(Collectors.toList());
      
        List<Transaction> transactions = transactionRepository.findTransactionsByClientIds(clientIds);
      
        Map<String, Object> result = new HashMap<>();
        result.put("clients", topClients);
      
        // Group transactions by client ID (as string)
        Map<String, List<Transaction>> transactionsByClient = new HashMap<>();
        for (Transaction tx : transactions) {
          String clientIdStr = tx.getClientId().toString(); // Convertir l'ObjectId en chaîne
          if (!transactionsByClient.containsKey(clientIdStr)) {
            transactionsByClient.put(clientIdStr, new ArrayList<>());
          }
          transactionsByClient.get(clientIdStr).add(tx);
        }
      
        result.put("transactions", transactionsByClient);
        return result;
      }
    }