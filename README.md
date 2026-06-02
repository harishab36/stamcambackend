# StamCam Backend

A Spring Boot REST API for managing legal deed documents, parties, users, roles, and zones. Built with MongoDB for persistence and iText 7 for PDF generation.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.3.0 |
| Language | Java 21 |
| Database | MongoDB |
| PDF Generation | iText 7.2.1 |
| API Documentation | SpringDoc OpenAPI (Swagger UI) |
| Boilerplate Reduction | Lombok |
| Build Tool | Maven |

---

## Prerequisites

- Java 21+
- Maven 3.8+
- MongoDB running on `localhost:27017`

---

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd StamCamBackend
```

### 2. Configure MongoDB

The app connects to `mongodb://localhost:27017/stamcam_db` by default.  
To override, edit `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/stamcam_db
```

For authenticated connections:

```properties
spring.data.mongodb.uri=mongodb://username:password@host:27017/stamcam_db
```

### 3. Run the application

```bash
mvn spring-boot:run
```

Server starts on **http://localhost:8080**.

### 4. Open Swagger UI

```
http://localhost:8080/api/v1/swagger-ui.html
```

Raw OpenAPI JSON:

```
http://localhost:8080/api/v1/docs
```

---

## Project Structure

```
src/main/java/org/fp/stamcam/
├── StamCamBackendApplication.java
├── config/
│   ├── SwaggerConfig.java
│   └── WebConfig.java               # CORS configuration
├── controllers/
│   ├── DeedController.java
│   ├── UserController.java
│   ├── RoleController.java
│   └── ZoneController.java
├── models/
│   ├── Deed.java
│   ├── DeedType.java                # Enum
│   ├── DeedStatus.java              # Enum
│   ├── Party.java
│   ├── PartyType.java               # Enum
│   ├── IdType.java                  # Enum
│   ├── Document.java
│   ├── User.java
│   ├── Role.java
│   ├── Screen.java
│   ├── Section.java
│   └── Zone.java
├── repositories/
│   ├── DeedRepository.java
│   ├── PartyRepository.java
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   └── ZoneRepository.java
├── services/
│   ├── DeedService.java
│   ├── PartyService.java
│   ├── PdfGenerationService.java
│   ├── UserService.java
│   ├── RoleService.java
│   └── ZoneService.java
├── utils/
│   ├── DeedIdGenerator.java
│   ├── PartyIdGenerator.java
│   ├── DocumentIdGenerator.java
│   ├── UserIdGenerator.java
│   ├── RoleIdGenerator.java
│   └── ZoneIdGenerator.java
└── exceptions/
    ├── GlobalExceptionHandler.java
    └── ZoneNotFoundException.java
```

---

## ID Format Convention

Every entity has an auto-generated prefixed ID assigned at creation time.

| Entity | Prefix | Example |
|---|---|---|
| Deed | `DD` | `DD00000001` |
| Party | `PT` | `PT00000001` |
| User | `USR` | `USR00000001` |
| Role | `ROL` | `ROL00000001` |
| Zone | `ZON` | `ZON00000001` |

---

## Data Models

### Deed
| Field | Type | Description |
|---|---|---|
| `id` | String | Auto-generated (`DD` + 8 digits) |
| `title` | String | Deed title |
| `matter` | String | Full text content of the deed |
| `type` | DeedType | Enum (see values below) |
| `parties` | List\<Party\> | Parties involved in the deed |
| `status` | DeedStatus | `DRAFT` / `IN_PROGRESS` / `COMPLETED` |
| `createdAt` | LocalDateTime | Auto-set on creation |
| `updatedAt` | LocalDateTime | Auto-updated on each save |

**DeedType values:** `SALE_DEED`, `GIFT_DEED`, `PROPERTY_TRANSFER_DEED`, `QUIT_CLAIM_DEED`, `DEED_OF_TRUST`, `POWER_OF_ATTORNEY_DEED`, `PARTNERSHIP_DEED`, `WILL_DEED`, `DONATION_DEED`, `MORTGAGE_DEED`, `LEASE_DEED`, `EXCHANGE_DEED`, `PARTITION_DEED`, `RELEASE_DEED`, `TRANSFER_DEED`, `AFFIDAVIT_DEED`

**DeedStatus values:** `DRAFT`, `IN_PROGRESS`, `COMPLETED`

### Party
| Field | Type | Description |
|---|---|---|
| `id` | String | Auto-generated (`PT` + 8 digits) |
| `name` | String | Full name |
| `emailId` | String | Email address |
| `phoneNumber` | String | Phone number |
| `idType` | IdType | Type of ID proof provided |
| `partyType` | PartyType | Role in the deed |

### User
| Field | Type | Description |
|---|---|---|
| `id` | String | Auto-generated (`USR` + 8 digits) |
| `username` | String | Unique username |
| `password` | String | Password (hash in production) |
| `createdAt` | LocalDateTime | Auto-set on creation |
| `updatedAt` | LocalDateTime | Auto-updated on each save |

### Role
| Field | Type | Description |
|---|---|---|
| `id` | String | Auto-generated (`ROL` + 8 digits) |
| `name` | String | Role name (e.g., `Admin`, `Editor`) |
| `allowedScreens` | List\<Screen\> | Screens this role has access to |

### Screen
| Field | Type | Description |
|---|---|---|
| `name` | String | Screen identifier |
| `route` | String | Frontend route (e.g., `/dashboard`) |
| `allowedSections` | List\<Section\> | Sections accessible within the screen |

### Section
| Field | Type | Description |
|---|---|---|
| `name` | String | Section identifier |
| `description` | String | Purpose of the section |

### Zone
| Field | Type | Description |
|---|---|---|
| `id` | String | Auto-generated (`ZON` + 8 digits) |
| `title` | String | Zone title |
| `description` | String | Zone description |

---

## API Reference

Base URL: `http://localhost:8080`

---

### Deeds — `/api/deeds`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/deeds` | Get all deeds |
| `GET` | `/api/deeds/{id}` | Get deed by ID |
| `POST` | `/api/deeds` | Create a new deed |
| `PUT` | `/api/deeds/{id}` | Update a deed |
| `DELETE` | `/api/deeds/{id}` | Delete a deed |
| `GET` | `/api/deeds/{id}/pdf` | Download deed as PDF |
| `GET` | `/api/deeds/count-by-status` | Count deeds grouped by status |
| `GET` | `/api/deeds/type/{type}` | Filter by deed type |
| `GET` | `/api/deeds/status/{status}` | Filter by deed status |
| `PATCH` | `/api/deeds/{id}/status/{newStatus}` | Update deed status only |
| `GET` | `/api/deeds/search/title/{title}` | Search by title (partial, case-insensitive) |
| `GET` | `/api/deeds/exact/title/{title}` | Get deed by exact title |
| `GET` | `/api/deeds/filter/type/{type}/title/{title}` | Filter by type + title |
| `GET` | `/api/deeds/filter/type/{type}/status/{status}` | Filter by type + status |
| `GET` | `/api/deeds/filter/status/{status}/title/{title}` | Filter by status + title |
| `GET` | `/api/deeds/search/party/name/{partyName}` | Find deeds by party name |
| `GET` | `/api/deeds/search/party/id/{partyId}` | Find deeds by party ID |
| `GET` | `/api/deeds/search/party/phone/{phoneNumber}` | Find deeds by party phone number |
| `GET` | `/api/deeds/search/party/idType/{idType}` | Find deeds by party ID type |
| `GET` | `/api/deeds/{deedId}/parties` | Get all parties on a deed |
| `GET` | `/api/deeds/{deedId}/parties/{partyId}` | Get a specific party on a deed |
| `POST` | `/api/deeds/{deedId}/parties` | Add a party to a deed |
| `PUT` | `/api/deeds/{deedId}/parties/{partyId}` | Update a party on a deed |
| `DELETE` | `/api/deeds/{deedId}/parties/{partyId}` | Remove a party from a deed |

---

### Users — `/api/users`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/users` | Get all users |
| `GET` | `/api/users/{id}` | Get user by ID |
| `POST` | `/api/users` | Create a new user |
| `PUT` | `/api/users/{id}` | Update a user |
| `DELETE` | `/api/users/{id}` | Delete a user |
| `GET` | `/api/users/username/{username}` | Get user by username |
| `POST` | `/api/users/login` | Authenticate with username and password |

---

### Roles — `/api/roles`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/roles` | Get all roles |
| `GET` | `/api/roles/{id}` | Get role by ID |
| `POST` | `/api/roles` | Create a new role |
| `PUT` | `/api/roles/{id}` | Update a role |
| `DELETE` | `/api/roles/{id}` | Delete a role |
| `GET` | `/api/roles/name/{name}` | Get role by exact name |
| `GET` | `/api/roles/search?name=` | Search roles by name (partial, case-insensitive) |
| `GET` | `/api/roles/exists?name=` | Check if a role name already exists |
| `GET` | `/api/roles/with-screens` | Get roles that have at least one screen |
| `GET` | `/api/roles/without-screens` | Get roles with no screens assigned |
| `GET` | `/api/roles/screen/name/{screenName}` | Get roles with access to a screen by exact name |
| `GET` | `/api/roles/screen/route?route=` | Get roles with access to a screen by route |
| `GET` | `/api/roles/screen/search?name=` | Search roles by screen name keyword |
| `GET` | `/api/roles/screen/{screenName}/count` | Count roles that have access to a screen |
| `GET` | `/api/roles/section/name/{sectionName}` | Get roles that include a specific section |
| `GET` | `/api/roles/section/search?name=` | Search roles by section name keyword |
| `GET` | `/api/roles/screen/{screenName}/section/{sectionName}` | Get roles by screen + section combination |
| `POST` | `/api/roles/{id}/screens` | Add a screen to a role |
| `DELETE` | `/api/roles/{id}/screens/{screenName}` | Remove a screen from a role |

---

### Zones — `/api/zones`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/zones` | Get all zones |
| `GET` | `/api/zones/{id}` | Get zone by ID |
| `POST` | `/api/zones` | Create a new zone |
| `PUT` | `/api/zones/{id}` | Update a zone |
| `DELETE` | `/api/zones/{id}` | Delete a zone |

---

## Example Requests

### Create a Deed

```json
POST /api/deeds
{
  "title": "Property Sale Agreement",
  "matter": "This deed is made between the parties...",
  "type": "SALE_DEED",
  "status": "DRAFT"
}
```

### Create a Role

```json
POST /api/roles
{
  "name": "Admin",
  "allowedScreens": [
    {
      "name": "Dashboard",
      "route": "/dashboard",
      "allowedSections": [
        { "name": "Overview", "description": "Summary statistics" },
        { "name": "Reports", "description": "Downloadable reports" }
      ]
    }
  ]
}
```

### Add a Party to a Deed

```json
POST /api/deeds/DD00000001/parties
{
  "name": "John Doe",
  "emailId": "john@example.com",
  "phoneNumber": "9876543210",
  "idType": "IDENTITY_PROOF",
  "partyType": "PARTY_ONE"
}
```

### User Login

```json
POST /api/users/login
{
  "username": "admin",
  "password": "secret"
}
```

---

## Logging

Logs are written to the console and to `logs/stamcam.log`.  
Files rotate at 10 MB and are retained for 7 days.

Log levels can be adjusted in `application.properties`:

```properties
logging.level.org.fp.stamcam=DEBUG
logging.level.org.springframework.data.mongodb=DEBUG
```

---

## Running Tests

Tests use Flapdoodle embedded MongoDB — no external database required.

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=PartyServiceTest
```

---

## Build

```bash
# Compile
mvn clean compile

# Package as JAR
mvn clean package

# Run the JAR directly
java -jar target/StamCamBackend-1.0-SNAPSHOT.jar

# Skip tests during packaging
mvn clean package -DskipTests
```

---

## Troubleshooting

**MongoDB connection timeout**
```bash
# Verify MongoDB is running
pgrep mongod

# Start MongoDB
mongod
```

**Port already in use**  
Change the port in `application.properties`:
```properties
server.port=8081
```

**Lombok not working in IDE**  
- IntelliJ IDEA: Settings → Plugins → Install Lombok
- VS Code: Install the Java Extension Pack