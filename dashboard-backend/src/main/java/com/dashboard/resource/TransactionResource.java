// TransactionResource.java
package com.dashboard.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.dashboard.model.Transaction;
import com.dashboard.service.TransactionService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transactions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {
    
    @Inject
    private TransactionService transactionService;
    
    @GET
    @Path("/client/{clientId}")
    public Response getClientTransactions(@PathParam("clientId") String clientId) {
        System.out.println("Received request for client ID: " + clientId);
        
        // Vérifier si l'ID est au format valide (24 caractères hexadécimaux)
        if (clientId == null || !clientId.matches("^[0-9a-fA-F]{24}$")) {
            System.err.println("Invalid client ID format: " + clientId);
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid client ID format: Must be 24 hexadecimal characters")
                        .build();
        }
        
        try {
            ObjectId objectId = new ObjectId(clientId);
            System.out.println("Successfully converted to ObjectId: " + objectId.toString());
            
            List<Transaction> transactions = transactionService.getClientTransactions(objectId);
            System.out.println("Found " + transactions.size() + " transactions for client ID: " + clientId);
            
            // Assurez-vous que les IDs sont correctement sérialisés en chaînes
            List<Map<String, Object>> formattedTransactions = transactions.stream()
                .map(tx -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tx.getId().toString());
                    map.put("clientId", tx.getClientId().toString());
                    map.put("amount", tx.getAmount());
                    map.put("date", tx.getDate());
                    map.put("productName", tx.getProductName());
                    map.put("quantity", tx.getQuantity());
                    return map;
                })
                .collect(Collectors.toList());
            
            return Response.ok(formattedTransactions).build();
        } catch (Exception e) {
            System.err.println("Error processing client ID: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Server error: " + e.getMessage())
                        .build();
        }
    }
    
    @GET
    @Path("/top-clients")
    public Response getTopClientsTransactions(@QueryParam("limit") @DefaultValue("5") int limit) {
        Map<String, Object> data = transactionService.getTopClientsTransactions(limit);
        return Response.ok(data).build();
    }
}