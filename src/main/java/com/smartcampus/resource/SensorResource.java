package com.smartcampus.resource;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Path("/api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().isEmpty()) return Response.status(400).entity("{\"error\":\"Sensor ID required\"}").build();
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) throw new LinkedResourceNotFoundException("Room '" + sensor.getRoomId() + "' not found.");
        if (DataStore.sensors.containsKey(sensor.getId())) return Response.status(409).entity("{\"error\":\"Sensor exists\"}").build();
        DataStore.sensors.put(sensor.getId(), sensor);
        Room room = DataStore.rooms.get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());
        return Response.status(201).entity(sensor).build();
    }
    @GET
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> result = new ArrayList<>(DataStore.sensors.values());
        if (type != null && !type.isEmpty()) result = result.stream().filter(s -> s.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
        return Response.ok(result).build();
    }
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) return Response.status(404).entity("{\"error\":\"Sensor not found\"}").build();
        return Response.ok(sensor).build();
    }
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
