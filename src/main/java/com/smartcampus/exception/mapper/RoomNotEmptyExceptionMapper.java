package com.smartcampus.exception.mapper;
import com.smartcampus.exception.RoomNotEmptyException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.Map;
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    public Response toResponse(RoomNotEmptyException e) {
        return Response.status(409).entity(Map.of("status", 409, "error", "Conflict", "message", e.getMessage())).type("application/json").build();
    }
}
