package com.dashboard.resource;

import com.dashboard.model.User;
import com.dashboard.service.AuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthResource {
    
    @Inject
    private AuthService authService;
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User credentials) {
        boolean isAuthenticated = authService.authenticate(
            credentials.getUsername(), 
            credentials.getPassword()
        );
        
        if (isAuthenticated) {
            return Response.ok(authService.getUser()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid username or password")
                .build();
        }
    }
}