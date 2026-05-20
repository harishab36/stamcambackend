# StamCamBackend - Spring Boot MongoDB Application

A Spring Boot REST API application for managing camera devices using MongoDB and Swagger/OpenAPI documentation.

## Features

- ✅ **Spring Boot 3.3.0** - Latest Spring Boot framework
- ✅ **MongoDB Integration** - Spring Data MongoDB for data persistence
- ✅ **REST API** - Complete CRUD operations for camera management
- ✅ **Swagger/OpenAPI** - Interactive API documentation at `/stamcam/api/v1/swagger-ui.html`
- ✅ **Lombok** - Reduce boilerplate code with annotations
- ✅ **Unit Tests** - Comprehensive test coverage with JUnit 5 and Mockito
- ✅ **Java 24** - Latest Java features enabled

## Project Structure

```
src/
├── main/
│   ├── java/org/fp/stamcam/
│   │   ├── StamCamBackendApplication.java      # Main Spring Boot application class
│   │   ├── config/
│   │   │   └── SwaggerConfig.java              # Swagger/OpenAPI configuration
│   │   ├── controllers/
│   │   │   └── CameraController.java           # REST endpoints for cameras
│   │   ├── models/
│   │   │   └── Camera.java                     # MongoDB document entity
│   │   ├── repositories/
│   │   │   └── CameraRepository.java           # MongoDB repository interface
│   │   └── services/
│   │       └── CameraService.java              # Business logic layer
│   └── resources/
│       └── application.properties              # Application configuration
└── test/
    └── java/org/fp/stamcam/
        └── services/
            └── CameraServiceTest.java          # Unit tests for CameraService
```

## Prerequisites

- **Java 24** or later
- **Maven 3.8+**
- **MongoDB 5.0+** (running locally or accessible via connection string)

## Getting Started

### 1. Clone and Navigate to Project

```bash
cd /Users/harishab36/Downloads/Backends/StamCamBackend
```

### 2. Build the Project

```bash
mvn clean compile
```

### 3. MongoDB Setup

Make sure MongoDB is running locally on the default port (27017):

```bash
# Using Docker (if Docker is installed)
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Or if MongoDB is installed locally
mongod
```

### 4. Run the Application

```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Package and run JAR
mvn clean package
java -jar target/StamCamBackend-1.0-SNAPSHOT.jar
```

The application will start on `http://localhost:8080/stamcam`

## API Endpoints

### Camera Management

- **GET** `/api/cameras` - Get all cameras
- **GET** `/api/cameras/{id}` - Get camera by ID
- **POST** `/api/cameras` - Create a new camera
- **PUT** `/api/cameras/{id}` - Update a camera
- **DELETE** `/api/cameras/{id}` - Delete a camera
- **GET** `/api/cameras/location/{location}` - Get cameras by location
- **GET** `/api/cameras/active/list` - Get all active cameras
- **GET** `/api/cameras/model/{model}` - Get cameras by model

## Swagger UI

Access the interactive API documentation at:

```
http://localhost:8080/stamcam/api/v1/swagger-ui.html
```

## Example API Requests

### Create a Camera

```bash
curl -X POST http://localhost:8080/stamcam/api/cameras \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Main Entrance",
    "model": "HD-1080P",
    "ipAddress": "192.168.1.10",
    "location": "Entrance",
    "active": true,
    "resolution": "1920x1080"
  }'
```

### Get All Cameras

```bash
curl http://localhost:8080/stamcam/api/cameras
```

### Get Active Cameras

```bash
curl http://localhost:8080/stamcam/api/cameras/active/list
```

### Update a Camera

```bash
curl -X PUT http://localhost:8080/stamcam/api/cameras/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Camera Name",
    "location": "New Location"
  }'
```

## Test Execution

Run all unit tests:

```bash
mvn test
```

Run a specific test class:

```bash
mvn test -Dtest=CameraServiceTest
```

## Database Configuration

Edit `src/main/resources/application.properties` to configure MongoDB connection:

```properties
# Default configuration (localhost)
spring.data.mongodb.uri=mongodb://localhost:27017/stamcam_db

# Or with authentication
spring.data.mongodb.uri=mongodb://username:password@host:port/stamcam_db
```

## Key Dependencies

- **spring-boot-starter-web** - Web and REST support
- **spring-boot-starter-data-mongodb** - MongoDB integration
- **springdoc-openapi-starter-webmvc-ui** - Swagger UI
- **lombok** - Java boilerplate reduction
- **junit-jupiter** - JUnit 5 testing framework
- **mockito** - Mocking framework for tests

## Maven Commands

```bash
# Compile
mvn clean compile

# Test
mvn test

# Package
mvn package

# Skip tests during packaging
mvn package -DskipTests

# View dependency tree
mvn dependency:tree

# Clean build artifacts
mvn clean

# Run the application
mvn spring-boot:run
```

## Troubleshooting

### MongoDB Connection Error

**Error**: `MongoTimeoutException: Timed out after 30000 ms`

**Solution**: Ensure MongoDB is running:
```bash
# Check if MongoDB is running
pgrep mongod

# Start MongoDB
mongod
```

### Java Version Error

**Error**: `Unsupported class file format`

**Solution**: Verify Java 24+ is installed:
```bash
java --version
```

### Port Already in Use

**Error**: `Address already in use - bind`

**Solution**: Change the port in `application.properties`:
```properties
server.port=8081
```

## Development Notes

- **Lombok**: Make sure your IDE has Lombok plugin installed for annotation processing
- **IntelliJ IDEA**: Install Lombok plugin from Settings → Plugins
- **Eclipse**: Install Lombok from https://projectlombok.org/setup/eclipse
- **VS Code**: Ensure Java extension is installed

## License

MIT License - See LICENSE file for details

## Next Steps

- Extend the Camera model with additional properties as needed
- Add authentication/authorization with Spring Security
- Integrate with frontend applications
- Add caching with Redis
- Deploy to cloud services (AWS, Azure, GCP)

