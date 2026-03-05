package com.dashboard.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.dashboard.soap.ClientSoapServiceImpl;
import com.sun.xml.ws.transport.http.servlet.WSServlet;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.xml.ws.Endpoint;

@Singleton
public class JettySoapConfig {
    
    @Inject
    private ClientSoapServiceImpl clientSoapService;
    
    private Endpoint endpoint;
    
    public void configureSoapEndpoint(Server server, ServletContextHandler context) {
        try {
            // Ajout du servlet pour gérer SOAP
            ServletHolder wsServletHolder = new ServletHolder(WSServlet.class);
            context.addServlet(wsServletHolder, "/soap/*");
            
            // Vérification du démarrage du contexte
            context.start();
            System.out.println("Jetty SOAP context started at: http://localhost:9090/soap/");
    
            // Publier le service SOAP
            String address = "http://localhost:9090/soap/ClientService";
            this.endpoint = Endpoint.publish(address, new ClientSoapServiceImpl());
            
            System.out.println("SOAP Endpoint published at: " + address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void stopEndpoint() {
        if (endpoint != null) {
            endpoint.stop();
            System.out.println("SOAP Endpoint stopped");
        }
    }
}