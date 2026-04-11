package com.smartcampus.exception.mapper;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    public Response toResponse(Throwable e) {
        return Response.status(500)
            .entity(Map.of("status", 500, "error", "Internal Server Error", "message", "An unexpected error occurred."))
            .type("application/json")
            .build();
    }
}
