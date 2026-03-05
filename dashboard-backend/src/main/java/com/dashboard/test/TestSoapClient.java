package com.dashboard.test;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;

public class TestSoapClient {

    // Interfaces locales correspondant aux types définis dans votre service SOAP
    public interface ClientDTO {
        String getId();
        String getName();
        String getEmail();
        double getPurchaseAmount();
        int getPurchaseCount();
    }
    
    public interface GenerationResult {
        String getMessage();
        long getCount();
    }
    
    public interface ResetResult {
        String getMessage();
        boolean isSuccess();
    }
    
    // Interface du service SOAP simplifiée
    public interface ClientSoapService {
        List<ClientDTO> getAllClients();
        List<ClientDTO> getTopClients(int limit);
        long getClientCount();
        GenerationResult generateClients(int count);
        ResetResult resetClients();
    }
    
    public static void main(String[] args) {
        try {
            // L'URL du WSDL
            URL wsdlUrl = new URL("http://localhost:9090/soap/ClientService?wsdl");
            
            // Créer un QName pour le service
            QName serviceName = new QName("http://soap.dashboard.com/", "ClientService");
            
            // Créer un Service
            Service service = Service.create(wsdlUrl, serviceName);
            
            // Obtenir un proxy pour le service SOAP
            ClientSoapService soapService = service.getPort(ClientSoapService.class);
            
            System.out.println("===== Test du service SOAP =====");
            
            // Test 1: Obtenir le nombre de clients
            try {
                System.out.println("\nTest 1: getClientCount()");
                long count = soapService.getClientCount();
                System.out.println("Nombre de clients: " + count);
            } catch (Exception e) {
                System.err.println("Erreur lors du test getClientCount(): " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test 2: Générer des clients si le nombre est 0
            try {
                System.out.println("\nTest 2: generateClients(100)");
                long count = soapService.getClientCount();
                if (count == 0) {
                    GenerationResult result = soapService.generateClients(100);
                    System.out.println("Résultat de la génération: " + result.getMessage());
                    System.out.println("Nombre de clients après génération: " + result.getCount());
                } else {
                    System.out.println("Des clients existent déjà, génération ignorée.");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du test generateClients(): " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test 3: Obtenir les meilleurs clients
            try {
                System.out.println("\nTest 3: getTopClients(5)");
                List<ClientDTO> topClients = soapService.getTopClients(5);
                System.out.println("Nombre de clients récupérés: " + topClients.size());
                
                for (ClientDTO client : topClients) {
                    System.out.println("- Client ID: " + client.getId());
                    System.out.println("  Nom: " + client.getName());
                    System.out.println("  Email: " + client.getEmail());
                    System.out.println("  Montant d'achats: " + client.getPurchaseAmount() + "€");
                    System.out.println("  Nombre d'achats: " + client.getPurchaseCount());
                    System.out.println();
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du test getTopClients(): " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test 4: Obtenir tous les clients
            try {
                System.out.println("\nTest 4: getAllClients()");
                List<ClientDTO> allClients = soapService.getAllClients();
                System.out.println("Nombre total de clients récupérés: " + allClients.size());
                // Afficher seulement les 3 premiers pour éviter une sortie trop verbeuse
                int displayCount = Math.min(3, allClients.size());
                for (int i = 0; i < displayCount; i++) {
                    ClientDTO client = allClients.get(i);
                    System.out.println("- Client " + (i+1) + ": " + client.getName());
                }
                if (allClients.size() > displayCount) {
                    System.out.println("... et " + (allClients.size() - displayCount) + " autres clients");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du test getAllClients(): " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test 5: Réinitialisation (décommentez si vous voulez tester)
            /*
            try {
                System.out.println("\nTest 5: resetClients()");
                ResetResult result = soapService.resetClients();
                System.out.println("Résultat de la réinitialisation: " + result.getMessage());
                System.out.println("Succès: " + result.isSuccess());
            } catch (Exception e) {
                System.err.println("Erreur lors du test resetClients(): " + e.getMessage());
                e.printStackTrace();
            }
            */
            
            System.out.println("\n===== Fin des tests =====");
            
        } catch (Exception e) {
            System.err.println("Erreur générale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}