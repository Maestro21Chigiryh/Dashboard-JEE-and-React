package com.dashboard.soap;

import java.util.List;
import java.util.stream.Collectors;

import com.dashboard.service.ClientService;

import jakarta.inject.Inject;
import jakarta.jws.WebService;

@WebService(
    serviceName = "ClientService",
    portName = "ClientServicePort",
    targetNamespace = "http://soap.dashboard.com/",
    endpointInterface = "com.dashboard.soap.ClientSoapService"
)
public class ClientSoapServiceImpl implements ClientSoapService {
    
    private final ClientService clientService;

    public ClientSoapServiceImpl() {
        // Pour CDI
        this.clientService = null;
    }
    
    @Inject
    public ClientSoapServiceImpl(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @Override
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients()
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClientDTO> getTopClients(int limit) {
        return clientService.getTopClients(limit)
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public long getClientCount() {
        return clientService.getClientCount();
    }
    
    @Override
    public GenerationResult generateClients(int count) {
        clientService.generateClients(count);
        long currentCount = clientService.getClientCount();
        return new GenerationResult("Generated " + count + " clients successfully", currentCount);
    }
    
    @Override
    public ResetResult resetClients() {
        try {
            clientService.resetClients();
            return new ResetResult("Reset clients successfully", true);
        } catch (Exception e) {
            return new ResetResult("Error: " + e.getMessage(), false);
        }
    }
}