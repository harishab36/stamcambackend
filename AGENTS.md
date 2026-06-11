# AGENTS.md - StamCamBackend Developer Guide

## Project Overview
**StamCamBackend** is a Spring Boot 3.3.0 REST API backend (Java 21, Maven, org.fp:StamCamBackend:1.0-SNAPSHOT) for managing legal deed documents, parties, users, roles, and zones. It uses MongoDB for persistence, iText 7 for PDF generation, and SpringDoc OpenAPI (Swagger UI) for API documentation.

**Key Framework Stack:**
- **Framework**: Spring Boot 3.3.0 with Spring Data MongoDB
- **Language**: Java 21
- **Database**: MongoDB
- **PDF Generation**: iText 7.2.1
- **API Documentation**: SpringDoc OpenAPI (Swagger UI) at `/api/v1/swagger-ui.html`
- **Boilerplate Reduction**: Lombok
- **Build Tool**: Maven with Java 21 target

## Directory Structure & Code Organization

### Core Locations
- `/src/main/java/org/fp/stamcam/` - Production Java source code organized by domain layers (controllers, services, repositories, models, config, exceptions, utils)
- `/src/main/resources/` - Configuration files (application.properties, environment-specific profiles)
- `/src/test/java/` - Test source code organized mirror to `/src/main/java/`
- `/pom.xml` - Maven project definition (declares Java 21 as source/target)

### Expected Package Setup
The core application package is `org.fp.stamcam` with the following established layers:
- **controllers**: REST endpoints (DeedController, PartyController, UserController, RoleController, ZoneController)
- **services**: Business logic (DeedService, PartyService, UserService, RoleService, ZoneService, PdfGenerationService)
- **repositories**: Data access layer extending Spring Data MongoDB repositories (DeedRepository, PartyRepository, UserRepository, RoleRepository, ZoneRepository)
- **models**: Domain entities and enums (Deed, Party, User, Role, Zone, Screen, Section, Document with corresponding Type/Status enums)
- **config**: Spring configuration (SwaggerConfig for API docs, WebConfig for CORS)
- **exceptions**: Global exception handling (GlobalExceptionHandler, ZoneNotFoundException)
- **utils**: ID generators following prefixed convention (DeedIdGenerator: `DD` prefix, PartyIdGenerator: `PT` prefix, UserIdGenerator: `USR` prefix, RoleIdGenerator: `ROL` prefix, ZoneIdGenerator: `ZON` prefix)

Test structure mirrors production at `src/test/java/org/fp/stamcam/`.

## Build, Test & Compilation Commands

### Prerequisites
Ensure you have:
- Java 21+ installed: `java --version`
- Maven 3.8+ installed: `mvn --version`
- MongoDB running on `localhost:27017` (default connection) or configured in `application.properties`

### Maven Workflows
```bash
# Compile project
mvn clean compile

# Run all tests (uses embedded Flapdoodle MongoDB; no external DB needed)
mvn test

# Run specific test class
mvn test -Dtest=YourTestClassName

# Package as JAR
mvn package

# Skip tests during packaging
mvn package -DskipTests

# Clean build artifacts
mvn clean
```

### Running the Application
```bash
# Run Spring Boot application locally (starts at http://localhost:8080)
mvn spring-boot:run

# Or run the packaged JAR directly
java -jar target/StamCamBackend-1.0-SNAPSHOT.jar
```

### API Documentation
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html` (configured in `springdoc.swagger-ui.path`)
- OpenAPI JSON: `http://localhost:8080/api/v1/docs` (configured in `springdoc.api-docs.path`)

## Java 21 & Project Specifics
- **Source/Target Version**: Java 21 (configured in `pom.xml` as `maven.compiler.source` and `maven.compiler.target`)
- **Spring Boot Version**: 3.3.0 with Spring Data MongoDB starter
- **Character Encoding**: UTF-8 (`project.build.sourceEncoding`)
- **MongoDB Connection**: Default is `mongodb://localhost:27017/stamcam_db` (configured in `src/main/resources/application.properties`)

### MongoDB Setup
To connect to MongoDB:
1. Ensure MongoDB is running: `pgrep mongod` (or start with `mongod`)
2. Configure connection in `src/main/resources/application.properties`:
   - Default URI: `spring.data.mongodb.uri=mongodb://localhost:27017/stamcam_db`
   - For authenticated connections: `spring.data.mongodb.uri=mongodb://username:password@host:27017/stamcam_db`
3. Tests use embedded Flapdoodle MongoDB (de.flapdoodle.embed.mongo.spring30x) - no external DB needed for test runs

## Key Development Patterns to Follow

### Adding Dependencies
Edit `pom.xml` - add `<dependency>` blocks within `<dependencies>` section. Common patterns:
- Web: Spring Boot (if REST API backend)
- Data: JDBC drivers, JPA/Hibernate
- Testing: JUnit 5, Mockito
- Example format:
  ```xml
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.9.3</version>
      <scope>test</scope>
  </dependency>
  ```

### Testing Convention
- Test class names end with `Test` or `Tests` suffix
- Place in `src/test/java/` matching production package structure
- Maven Surefire plugin automatically runs `*Test.java` classes

### Configuration Files
- Place application properties in `src/main/resources/`:
  - `application.properties` - Base configuration (default profile)
  - `application-dev.properties` - Development profile (use with `--spring.profiles.active=dev`)
  - `application-prod.properties` - Production profile (use with `--spring.profiles.active=prod`)
- Key configurations:
  - **Server Port**: `server.port=8080` (default)
  - **MongoDB URI**: `spring.data.mongodb.uri=mongodb://localhost:27017/stamcam_db`
  - **Logging**: `logging.file.name=logs/stamcam.log` with 10MB rolling files, 7-day retention
  - **Swagger APIs**: `springdoc.api-docs.path=/api/v1/docs`, `springdoc.swagger-ui.path=/api/v1/swagger-ui.html`
- Access properties via Spring's `@Value` or `@ConfigurationProperties` in code

## Critical Tasks & Debugging

### Troubleshooting Builds
- **Java Version Error**: Verify Java 21+ is installed: `java --version`
- **MongoDB Connection Failed**: 
  - Verify MongoDB is running: `pgrep mongod`
  - Start MongoDB: `mongod` (or use Docker: `docker run -d -p 27017:27017 mongo`)
  - Check URI in `application.properties`: default is `mongodb://localhost:27017/stamcam_db`
- **Dependency Issues**: Run `mvn dependency:tree` to inspect dependency resolution
- **Stale Artifacts**: Run `mvn clean` before retry
- **IDE Sync Issues**: Reimport Maven project in IDE (IntelliJ: Maven panel → Reimport)
- **Swagger not loading**: Verify Swagger paths in `application.properties` match endpoints and SpringDoc configuration in `config/SwaggerConfig.java`

### Project Structure Reference
The project is **fully implemented** with active development. When extending:
1. **Controllers** handle REST endpoints and delegate to services (established pattern in DeedController, PartyController, etc.)
2. **Services** contain business logic and orchestration; PdfGenerationService handles iText PDF generation
3. **Repositories** extend MongoRepository for CRUD operations on MongoDB collections
4. **Models** are annotated with `@Document` for MongoDB persistence and use Lombok `@Builder`, `@Data` for boilerplate reduction
5. **ID Generation**: All entities follow prefixed auto-increment pattern (see **ID Format Convention** below)
6. **Global Exception Handling**: Use GlobalExceptionHandler for consistent error responses (e.g., ZoneNotFoundException)

## ID Format Convention
Every entity in the application follows a prefixed auto-increment ID pattern for easy identification:
- **Deed**: `DD` prefix (e.g., `DD00000001`)
- **Party**: `PT` prefix (e.g., `PT00000001`)
- **User**: `USR` prefix (e.g., `USR00000001`)
- **Role**: `ROL` prefix (e.g., `ROL00000001`)
- **Zone**: `ZON` prefix (e.g., `ZON00000001`)

IDs are auto-generated at entity creation time using dedicated generator classes in `utils/` package (DeedIdGenerator, PartyIdGenerator, etc.). This pattern is discoverable in the codebase and essential for understanding MongoDB document structure.

## Git & Ignored Artifacts
- `target/` directory is ignored - safe to delete, Maven regenerates
- `.idea/`, `.vscode/`, IDE-specific folders ignored
- `.DS_Store` ignored on macOS
- Only commit source code, pom.xml, and documentation

## IDE Integration Notes
- **IntelliJ IDEA**: `pom.xml` auto-detected; right-click project → "Reimport". Install Lombok plugin for annotations support.
- **Spring Boot Launcher**: Use "Run" configuration to launch `mvn spring-boot:run` or set up IDE Spring Boot run configuration
- **Maven Plugin**: Available in `.mvn/` wrapper; prefer `mvn` CLI for consistency
- **Debugging**: Set breakpoints and use IDE debugger with `mvn spring-boot:run -Dspring-boot.run.arguments="--debug"`
- **Hot Reload**: Spring DevTools is not explicitly configured; full recompile via `mvn compile` required for class changes
- **Swagger Testing**: Open `http://localhost:8080/api/v1/swagger-ui.html` in browser after `mvn spring-boot:run` to test endpoints interactively

