package com.dashboard.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dashboard.model.Client;
import com.dashboard.service.ClientService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {
    
    @Inject
    private ClientService clientService;
    
    @GET
    public Response getAllClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            return Response.ok(clients).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving clients: " + e.getMessage())
                    .build();
        }
    }
    
// Dans ClientResource.java (ou là où vous renvoyez la liste des top clients)

@GET
@Path("/top")
public Response getTopClients(@QueryParam("limit") @DefaultValue("5") int limit) {
    List<Client> topClients = clientService.getTopClients(limit);
    
    // Convertir en format JSON avec les IDs au format string
    List<Map<String, Object>> formattedClients = topClients.stream()
        .map(client -> {
            Map<String, Object> map = new HashMap<>();
            
            // Garantir que l'ID est une chaîne
            map.put("id", client.getId().toString());
            
            // Ajouter les autres propriétés
            map.put("name", client.getName());
            map.put("email", client.getEmail());
            map.put("phone", client.getPhone());
            map.put("address", client.getAddress());
            map.put("purchaseAmount", client.getPurchaseAmount());
            map.put("purchaseCount", client.getPurchaseCount());
            map.put("registrationDate", client.getRegistrationDate());
            map.put("lastPurchaseDate", client.getLastPurchaseDate());
            map.put("active", client.isActive());
            
            return map;
        })
        .collect(Collectors.toList());
    
    return Response.ok(formattedClients).build();
}
    
    @GET
    @Path("/count")
    public Response getClientCount() {
        try {
            long count = clientService.getClientCount();
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving client count: " + e.getMessage())
                    .build();
        }
    }
    
    @POST
    @Path("/generate")
    public Response generateClients(@QueryParam("count") @DefaultValue("1000") int count) {
        clientService.generateClients(count);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Generated " + count + " clients successfully");
        response.put("count", clientService.getClientCount());
        
        return Response.ok(response).build();
    }
    
    @DELETE
    public Response resetClients() {
        try {
            clientService.resetClients();
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reset clients successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error resetting clients: " + e.getMessage())
                    .build();
        }
    }
}