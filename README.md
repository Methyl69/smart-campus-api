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

