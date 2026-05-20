# QuickStart Guide - StamCamBackend

## What Was Generated

Your project now has a complete Spring Boot application with:

### ✅ **Project Structure**
```
StamCamBackend/
├── pom.xml                                  # Maven configuration with Spring Boot 3.3.0
├── README.md                                # Complete API documentation
├── QUICKSTART.md                            # This file - First-time setup guide  
├── IMPLEMENTATION_SUMMARY.md                # Project overview & architecture
├── AGENTS.md                                # AI agent guidelines
│
├── src/main/java/org/fp/stamcam/
│   ├── StamCamBackendApplication.java       # Spring Boot main application class
│   ├── config/
│   │   └── SwaggerConfig.java               # OpenAPI/Swagger UI configuration
│   ├── controllers/
│   │   └── CameraController.java            # REST API endpoints (8 total)
│   ├── models/
│   │   └── Camera.java                      # MongoDB document entity
│   ├── repositories/
│   │   └── CameraRepository.java            # Spring Data MongoDB repository
│   └── services/
│       └── CameraService.java               # Business logic layer
├── src/main/resources/
│   └── application.properties               # Server, MongoDB, Swagger config
└── src/test/java/org/fp/stamcam/
    └── services/
        └── CameraServiceTest.java           # Unit tests with Mockito mocking
```

### 📦 **Key Dependencies Included**
- **Spring Boot 3.3.0** - Web framework with embedded Tomcat
- **Spring Data MongoDB** - Repository abstraction for MongoDB
- **Springdoc OpenAPI 2.3.0** - Swagger UI integration
- **Lombok** - Reduce boilerplate with @Data, @Builder, @AllArgsConstructor
- **JUnit 5 + Mockito** - Unit testing framework
- **Embedded MongoDB** - In-memory MongoDB for testing

### 🏗️ **Core Java Classes**
- **StamCamBackendApplication** - Main entry point with @SpringBootApplication
- **CameraController** - 8 REST endpoints with full Swagger documentation
- **CameraService** - Business logic: CRUD and filtering operations
- **CameraRepository** - Spring Data MongoDB interface with custom queries
- **Camera** - MongoDB @Document entity with Lombok annotations
- **SwaggerConfig** - OpenAPI bean customization with API info and contact details
- **CameraServiceTest** - Unit tests using Mockito mocks and JUnit 5

## First-Time Setup

### Core Classes Overview

Before setup, understand the main components:

| Class | Role | Location |
|-------|------|----------|
| **StamCamBackendApplication** | Spring Boot entry point | `org.fp.stamcam` |
| **CameraController** | REST API endpoints (8 total) | `org.fp.stamcam.controllers` |
| **CameraService** | Business logic & database operations | `org.fp.stamcam.services` |
| **CameraRepository** | MongoDB data access (Spring Data) | `org.fp.stamcam.repositories` |
| **Camera** | MongoDB document entity | `org.fp.stamcam.models` |
| **SwaggerConfig** | API documentation configuration | `org.fp.stamcam.config` |

### Step 1: Sync Maven Dependencies
In IntelliJ IDEA:
1. Right-click on `pom.xml` → Select **"Maven"** → **"Reload project"**
2. Or use: Maven panel → Reimport
3. Wait for dependencies to download (~2-3 minutes on first build)

### Step 2: Ensure MongoDB is Running
```bash
# Using Docker (easiest)
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Or if MongoDB is installed locally
mongod
```

MongoDB should be accessible at `mongodb://localhost:27017`

### Step 3: Build and Run
```bash
# Navigate to project directory
cd /Users/harishab36/Downloads/Backends/StamCamBackend

# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Or package and run as JAR
mvn clean package
java -jar target/StamCamBackend-1.0-SNAPSHOT.jar
```

### Step 4: Access the Application
- **API Base URL**: `http://localhost:8080/stamcam`
- **Swagger UI**: `http://localhost:8080/stamcam/api/v1/swagger-ui.html`
- **API Docs JSON**: `http://localhost:8080/stamcam/api/v1/docs`

## IDE Integration

### IntelliJ IDEA (Recommended)
1. Open the project folder
2. Right-click `pom.xml` → "Add as Maven Project"
3. Language Level: Set to Java 24 (File → Project Settings → Project → Language Level)
4. SDKs: Ensure JDK 24+ is configured

### VS Code
1. Install "Extension Pack for Java" (Microsoft)
2. Install "Maven for Java"
3. The project will auto-detect and configure

## Troubleshooting

### Maven Dependencies Not Downloaded
**Solution**: 
```bash
mvn clean install
mvn -U clean compile  # Force update
```

### Can't Find Spring Symbols
**Solution**: This resolves once Maven downloads dependencies. Force refresh:
- IntelliJ: File → Invalidate Caches → Reload
- VS Code: Java: Clean Language Server Workspace

### MongoDB Connection Failed
**Ensure MongoDB is running:**
```bash
# Check if running
pgrep mongod

# If not, start it
mongod  # or use Docker command above
```

### Port 8080 Already in Use
**Edit `src/main/resources/application.properties`:**
```properties
server.port=8081
```

## Available API Endpoints

### CRUD Operations (5 endpoints)
- `POST /api/cameras` - Create new camera
- `GET /api/cameras` - Get all cameras
- `GET /api/cameras/{id}` - Get camera by ID
- `PUT /api/cameras/{id}` - Update existing camera
- `DELETE /api/cameras/{id}` - Delete camera

### Advanced Filtering (3 endpoints)
- `GET /api/cameras/active/list` - Get all active cameras
- `GET /api/cameras/location/{location}` - Filter cameras by location
- `GET /api/cameras/model/{model}` - Filter cameras by model

## Test the API

### Using cURL
```bash
# Create a camera
curl -X POST http://localhost:8080/stamcam/api/cameras \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Main Gate",
    "model": "HD-1080P",
    "ipAddress": "192.168.1.100",
    "location": "Gate",
    "active": true,
    "resolution": "1920x1080"
  }'

# Get all cameras
curl http://localhost:8080/stamcam/api/cameras

# Get active cameras
curl http://localhost:8080/stamcam/api/cameras/active/list
```

### Using Swagger UI
Navigate to `http://localhost:8080/stamcam/api/v1/swagger-ui.html` and test endpoints interactively.

## Run Tests
```bash
# All tests
mvn test

# Specific test
mvn test -Dtest=CameraServiceTest

# Skip tests during packaging
mvn package -DskipTests
```

## Next Steps

1. **Start the application** and verify it runs without errors
2. **Test an API endpoint** using Swagger UI or cURL
3. **Add more entity classes** following the Camera pattern
4. **Extend the service layer** with more business logic
5. **Add authentication** using Spring Security if needed
6. **Configure MongoDB** for production databases

## Documentation Files
- **README.md** - Complete API documentation and feature details
- **QUICKSTART.md** - This file - First-time setup and troubleshooting
- **IMPLEMENTATION_SUMMARY.md** - Project architecture and overview
- **AGENTS.md** - AI agent development guidelines
- **pom.xml** - Maven configuration with all dependencies

---
**Ready to start?** Run `mvn spring-boot:run` and navigate to the Swagger UI! 🚀
