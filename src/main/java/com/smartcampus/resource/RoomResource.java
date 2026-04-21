package com.smartcampus.resource;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;

@Path("/api/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getAllRooms() {
        return Response.ok(new ArrayList<>(DataStore.rooms.values())).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty())
            return Response.status(400).entity("{\"error\":\"Room ID required\"}").build();
        if (DataStore.rooms.containsKey(room.getId()))
            return Response.status(409).entity("{\"error\":\"Room already exists\"}").build();
        DataStore.rooms.put(room.getId(), room);
        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null)
            return Response.status(404).entity("{\"error\":\"Room not found\"}").build();
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null)
            return Response.status(404).entity("{\"error\":\"Room not found\"}").build();
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty())
            throw new RoomNotEmptyException("Room " + roomId + " still has sensors.");
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}