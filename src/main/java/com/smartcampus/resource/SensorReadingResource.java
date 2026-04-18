package com.smartcampus.resource;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private final String sensorId;
    public SensorReadingResource(String sensorId) { this.sensorId = sensorId; }
    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) return Response.status(404).entity("{\"error\":\"Sensor not found\"}").build();
        List<SensorReading> history = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(history).build();
    }
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) return Response.status(404).entity("{\"error\":\"Sensor not found\"}").build();
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) throw new SensorUnavailableException("Sensor " + sensorId + " is under maintenance.");
        if (reading.getId() == null || reading.getId().isEmpty()) reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());
        DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
        sensor.setCurrentValue(reading.getValue());
        return Response.status(201).entity(reading).build();
    }
}
