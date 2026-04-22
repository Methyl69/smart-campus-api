package com.smartcampus.resource;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.LinkedHashMap;
import java.util.Map;
@Path("/api/v1")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("version", "1.0");
        info.put("description", "Smart Campus Sensor & Room Management API");
        info.put("contact", "admin@smartcampus.ac.uk");
        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        info.put("resources", links);
        Map<String, String> hateoas = new LinkedHashMap<>();
        hateoas.put("self", "/api/v1");
        hateoas.put("rooms", "/api/v1/rooms");
        hateoas.put("sensors", "/api/v1/sensors");
        info.put("links", hateoas);
        return Response.ok(info).build();
    }
}
