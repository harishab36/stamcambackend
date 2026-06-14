# DeedTitle & PartyTitle Quick Reference Guide

## Entity Structure

### PartyTitle
```
ID: PRT0001 (format: PRT + 4 digits)
Fields:
  - id: String (auto-generated)
  - title: String (e.g., "Buyer", "Seller", "Witness")
  - createdAt: LocalDateTime (auto-set)
  - updatedAt: LocalDateTime (auto-set)
```

### DeedTitle
```
ID: DDTL0001 (format: DDTL + 4 digits)
Fields:
  - id: String (auto-generated)
  - title: String (e.g., "Sale Deed", "Gift Deed")
  - partyTitles: List<PartyTitle> (embedded list)
  - createdAt: LocalDateTime (auto-set)
  - updatedAt: LocalDateTime (auto-set)
```

## REST API Endpoints

### PartyTitle Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/party-titles` | Get all party titles |
| GET | `/api/party-titles/{id}` | Get by ID |
| GET | `/api/party-titles/search?title={title}` | Search by title |
| POST | `/api/party-titles?title={title}` | Create new |
| PUT | `/api/party-titles/{id}?title={title}` | Update |
| DELETE | `/api/party-titles/{id}` | Delete |

### DeedTitle Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/deed-titles` | Get all deed titles |
| GET | `/api/deed-titles/{id}` | Get by ID |
| GET | `/api/deed-titles/search?title={title}` | Search by title |
| POST | `/api/deed-titles?title={title}` | Create new |
| PUT | `/api/deed-titles/{id}?title={title}` | Update |
| DELETE | `/api/deed-titles/{id}` | Delete |

### DeedTitle Party Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/deed-titles/{deedTitleId}/party-titles/{partyTitleId}` | Add party to deed |
| DELETE | `/api/deed-titles/{deedTitleId}/party-titles/{partyTitleId}` | Remove party from deed |
| PUT | `/api/deed-titles/{deedTitleId}/party-titles/{partyTitleId}?newTitle={title}` | Update party in deed |
| GET | `/api/deed-titles/{deedTitleId}/party-titles` | Get all parties in deed |

## Service Methods

### PartyTitleService

```java
// Create
PartyTitle createPartyTitle(String title);

// Read
Optional<PartyTitle> getPartyTitleById(String id);
List<PartyTitle> getAllPartyTitles();
List<PartyTitle> getPartyTitlesByTitle(String title);
Optional<PartyTitle> getPartyTitleByExactTitle(String title);

// Update
Optional<PartyTitle> updatePartyTitle(String id, String title);

// Delete
boolean deletePartyTitle(String id);
```

### DeedTitleService

```java
// CRUD Operations
DeedTitle createDeedTitle(String title);
Optional<DeedTitle> getDeedTitleById(String id);
List<DeedTitle> getAllDeedTitles();
List<DeedTitle> getDeedTitlesByTitle(String title);
Optional<DeedTitle> getDeedTitleByExactTitle(String title);
Optional<DeedTitle> updateDeedTitle(String id, String title);
boolean deleteDeedTitle(String id);

// Party Title Management
Optional<DeedTitle> addPartyTitle(String deedTitleId, String partyTitleId);
Optional<DeedTitle> removePartyTitle(String deedTitleId, String partyTitleId);
Optional<DeedTitle> updatePartyTitleInDeed(String deedTitleId, String partyTitleId, String newTitle);
List<PartyTitle> getPartyTitlesForDeed(String deedTitleId);
```

## ID Generators

### PartyTitleIdGenerator
```java
String generatePartyTitleId();  // Returns next ID (e.g., "PRT0002")
boolean isValidPartyTitleId(String id);
long extractNumber(String id);
```

### DeedTitleIdGenerator
```java
String generateDeedTitleId();  // Returns next ID (e.g., "DDTL0002")
boolean isValidDeedTitleId(String id);
long extractNumber(String id);
```

## Repositories

### PartyTitleRepository
```java
Optional<PartyTitle> findByTitle(String title);
List<PartyTitle> findByTitleContainingIgnoreCase(String title);
```

### DeedTitleRepository
```java
Optional<DeedTitle> findByTitle(String title);
List<DeedTitle> findByTitleContainingIgnoreCase(String title);
```

## Usage Workflow

### Step 1: Create Party Titles
```bash
curl -X POST "http://localhost:8080/api/party-titles?title=Buyer"
curl -X POST "http://localhost:8080/api/party-titles?title=Seller"
curl -X POST "http://localhost:8080/api/party-titles?title=Witness"
```

Response:
```json
{"id": "PRT0001", "title": "Buyer"}
{"id": "PRT0002", "title": "Seller"}
{"id": "PRT0003", "title": "Witness"}
```

### Step 2: Create a Deed Title
```bash
curl -X POST "http://localhost:8080/api/deed-titles?title=Sale%20Deed"
```

Response:
```json
{
  "id": "DDTL0001",
  "title": "Sale Deed",
  "partyTitles": []
}
```

### Step 3: Add Party Titles to Deed
```bash
curl -X POST "http://localhost:8080/api/deed-titles/DDTL0001/party-titles/PRT0001"
curl -X POST "http://localhost:8080/api/deed-titles/DDTL0001/party-titles/PRT0002"
```

Response:
```json
{
  "id": "DDTL0001",
  "title": "Sale Deed",
  "partyTitles": [
    {"id": "PRT0001", "title": "Buyer"},
    {"id": "PRT0002", "title": "Seller"}
  ]
}
```

### Step 4: Update Party in Deed
```bash
curl -X PUT "http://localhost:8080/api/deed-titles/DDTL0001/party-titles/PRT0001?newTitle=Co-Buyer"
```

### Step 5: Remove Party from Deed
```bash
curl -X DELETE "http://localhost:8080/api/deed-titles/DDTL0001/party-titles/PRT0003"
```

## MongoDB Collections

### partyTitles Collection
```json
{
  "_id": "PRT0001",
  "title": "Buyer",
  "createdAt": ISODate("2026-06-11T00:00:00Z"),
  "updatedAt": ISODate("2026-06-11T00:00:00Z")
}
```

### deedTitles Collection
```json
{
  "_id": "DDTL0001",
  "title": "Sale Deed",
  "partyTitles": [
    {
      "_id": "PRT0001",
      "title": "Buyer",
      "createdAt": ISODate("2026-06-11T00:00:00Z"),
      "updatedAt": ISODate("2026-06-11T00:00:00Z")
    }
  ],
  "createdAt": ISODate("2026-06-11T00:00:00Z"),
  "updatedAt": ISODate("2026-06-11T00:00:00Z")
}
```

## File Locations

```
src/main/java/org/fp/stamcam/
├── models/
│   ├── DeedTitle.java
│   └── PartyTitle.java
├── repositories/
│   ├── DeedTitleRepository.java
│   └── PartyTitleRepository.java
├── services/
│   ├── DeedTitleService.java
│   └── PartyTitleService.java
├── controllers/
│   ├── DeedTitleController.java
│   └── PartyTitleController.java
└── utils/
    ├── DeedTitleIdGenerator.java
    └── PartyTitleIdGenerator.java
```

## Testing with Swagger

1. Start the application: `mvn spring-boot:run`
2. Open browser: `http://localhost:8080/api/v1/swagger-ui.html`
3. Find "Party Title Management" and "Deed Title Management" sections
4. Use the Try it out buttons to test endpoints

## Features

✅ Auto-generated IDs (PRT0001, DDTL0001, etc.)
✅ MongoDB persistence
✅ RESTful API
✅ Swagger/OpenAPI documentation
✅ Case-insensitive search
✅ Add/Remove/Update party titles in deeds
✅ Duplicate prevention
✅ Timestamp tracking
✅ Spring Data MongoDB integration

