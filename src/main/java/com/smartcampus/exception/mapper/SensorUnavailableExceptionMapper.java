package com.smartcampus.exception.mapper;
import com.smartcampus.exception.SensorUnavailableException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.Map;
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    public Response toResponse(SensorUnavailableException e) {
        return Response.status(403).entity(Map.of("status", 403, "error", "Forbidden", "message", e.getMessage())).type("application/json").build();
    }
}
