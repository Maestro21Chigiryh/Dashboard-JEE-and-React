package com.dashboard.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class RootResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response home() {
        return Response.ok("Bienvenue sur l'API du Dashboard!").build();
    }
}
