package com.smartcampus.exception.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    public Response toResponse(LinkedResourceNotFoundException e) {
        return Response.status(422)
            .entity(Map.of("status", 422, "error", "Unprocessable Entity", "message", e.getMessage()))
            .type("application/json")
            .build();
    }
}
