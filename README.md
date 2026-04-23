# Smart Campus API - 5COSC022W Coursework

## Overview
A RESTful API built with JAX-RS (Jersey) and Grizzly embedded server to manage Rooms, Sensors, and Sensor Readings for the University Smart Campus initiative. All data is stored in-memory using ConcurrentHashMap. No database or Spring Boot is used.

## Technology Stack
- Java 11
- JAX-RS (Jakarta RESTful Web Services)
- Jersey 3.1.3 (JAX-RS implementation)
- Grizzly HTTP Server (embedded server)
- Jackson (JSON serialization)
- Maven (build tool)

## How to Build and Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Steps
1. Clone the repository: git clone https://github.com/Methyl69/smart-campus-api.git
2. Navigate into the folder: cd smart-campus-api
3. Build the project: mvn clean package
4. Run the server: mvn exec:java
5. API is live at: http://localhost:8080/api/v1

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1 | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a room |
| GET | /api/v1/rooms/{roomId} | Get room by ID |
| DELETE | /api/v1/rooms/{roomId} | Delete room |
| GET | /api/v1/sensors | Get all sensors |
| POST | /api/v1/sensors | Create a sensor |
| GET | /api/v1/sensors/{sensorId} | Get sensor by ID |
| DELETE | /api/v1/sensors/{sensorId} | Delete sensor |
| GET | /api/v1/sensors/{sensorId}/readings | Get reading history |
| POST | /api/v1/sensors/{sensorId}/readings | Add new reading |

## Sample curl Commands

### 1. Discovery endpoint
curl http://localhost:8080/api/v1

### 2. Create a room
curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LIB-301\",\"name\":\"Library\",\"capacity\":50}"

### 3. Get all rooms
curl http://localhost:8080/api/v1/rooms

### 4. Create a sensor
curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"LIB-301\"}"

### 5. Filter sensors by type
curl http://localhost:8080/api/v1/sensors?type=Temperature

### 6. Add a sensor reading
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d "{\"value\":22.5}"

### 7. Get reading history
curl http://localhost:8080/api/v1/sensors/TEMP-001/readings

### 8. Delete room with no sensors
curl -X DELETE http://localhost:8080/api/v1/rooms/EMPTY-101

### 9. Try to delete room with sensors (409 error)
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301

### 10. Try sensor with invalid roomId (422 error)
curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"FAKE-ROOM\"}"

## Report - Question Answers

### Part 1.1 - JAX-RS Lifecycle
By default JAX-RS creates a new resource class instance per request (request-scoped). Instance variables reset every request so cannot hold shared state. All data is stored in static ConcurrentHashMap fields in DataStore.java to safely share state across requests. ConcurrentHashMap prevents race conditions when multiple requests arrive simultaneously.

### Part 1.2 - HATEOAS
HATEOAS means the API includes links to related resources in responses, making it self-documenting. Clients navigate dynamically using links rather than hardcoding URLs. This reduces coupling, allows the API to evolve without breaking clients, and removes reliance on static documentation.

### Part 2.1 - IDs vs Full Objects
Returning only IDs reduces payload size but forces N extra requests to fetch each object. Returning full objects increases payload but saves round trips. For large collections IDs are better for bandwidth. Full objects suit small collections where clients need all fields immediately.

### Part 2.2 - Idempotency of DELETE
Yes, DELETE is idempotent. First call deletes the room and returns 204. Subsequent calls return 404. In both cases the end state is the same - the room does not exist - satisfying idempotency.

### Part 3.1 - @Consumes Mismatch
If a client sends text/plain or application/xml to a @Consumes(APPLICATION_JSON) endpoint, JAX-RS returns 415 Unsupported Media Type automatically. The request never reaches the resource method.

### Part 3.2 - QueryParam vs PathParam
Query parameters are correct for filtering because they are optional and do not change resource identity. /sensors always means the full collection. Path parameters imply a distinct resource at that path which is semantically wrong for filtering. Query params also allow combining multiple filters easily.

### Part 4.1 - Sub-Resource Locator Benefits
Delegates handling to a separate class keeping each class focused on one responsibility. Avoids one massive unreadable controller. Separate classes like SensorReadingResource can be developed and tested independently improving maintainability.

### Part 5.2 - Why 422 over 404
404 means the URL was not found. 422 means the URL is valid but the payload references something that does not exist. The /sensors endpoint exists - the problem is the roomId inside the payload cannot be resolved. 422 communicates this semantic distinction accurately.

### Part 5.4 - Security Risks of Stack Traces
Stack traces expose internal file paths, class names, library versions and application logic. Attackers use this to find known vulnerabilities, understand architecture for targeted attacks, and identify unhandled edge cases. GlobalExceptionMapper prevents this by returning only a generic 500 message.

### Part 5.5 - Filters vs Manual Logging
Filters are written once and apply automatically to every request. Manual logging in every method is repetitive, easy to forget, and hard to maintain. If logging format changes, a filter requires editing one file instead of dozens of methods.
