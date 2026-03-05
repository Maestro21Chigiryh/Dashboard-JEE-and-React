package com.dashboard;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import com.dashboard.config.JettySoapConfig;

public class DashboardApplication {
    
    public static void main(String[] args) {
        try {
            // Initialiser Weld (CDI)
            Weld weld = new Weld();
            WeldContainer container = weld.initialize();
            
            // Récupérer l'instance de l'application depuis le conteneur CDI
            DashboardApplication app = container.select(DashboardApplication.class).get();
            app.start(container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void start(WeldContainer container) throws Exception {
        // Créer et configurer le serveur Jetty
        Server server = new Server(9090); // Assurez-vous que le port est cohérent
        
        // Configurer le contexte servlet
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        
        // Configurer Jersey pour REST
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("com.dashboard.resource");
        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(resourceConfig));
        context.addServlet(jerseyServlet, "/api/*");
        
        // Attacher le contexte au serveur
        server.setHandler(context);
        
        // Obtenir la configuration SOAP depuis le conteneur CDI
        JettySoapConfig soapConfig = container.select(JettySoapConfig.class).get();
        soapConfig.configureSoapEndpoint(server, context);
        
        // Démarrer le serveur
        server.start();
        System.out.println("Server started at http://localhost:9090/");
        
        // Attendre que le serveur se termine
        server.join();
    }
}