# DeedTitle and PartyTitle Implementation Summary

## Overview
Successfully created two new entities (`DeedTitle` and `PartyTitle`) with custom ID generation, repositories, services, and REST controllers. These entities support managing deed templates with configurable party titles.

## Entities Created

### 1. PartyTitle Entity
**File**: `src/main/java/org/fp/stamcam/models/PartyTitle.java`
- **Collection**: `partyTitles`
- **ID Format**: `PRT` + 4 digits (e.g., `PRT0001`, `PRT0002`)
- **Fields**:
  - `id` (String): Unique identifier with PRT prefix
  - `title` (String): Title of the party (e.g., Buyer, Seller, Witness)
  - `createdAt` (LocalDateTime): Auto-populated creation timestamp
  - `updatedAt` (LocalDateTime): Auto-populated last modified timestamp
- **Annotations**: `@Document`, `@Data`, `@Builder`, Lombok annotations

### 2. DeedTitle Entity
**File**: `src/main/java/org/fp/stamcam/models/DeedTitle.java`
- **Collection**: `deedTitles`
- **ID Format**: `DDTL` + 4 digits (e.g., `DDTL0001`, `DDTL0002`)
- **Fields**:
  - `id` (String): Unique identifier with DDTL prefix
  - `title` (String): Title of the deed template (e.g., Sale Deed, Gift Deed)
  - `partyTitles` (List<PartyTitle>): List of party titles in this deed
  - `createdAt` (LocalDateTime): Auto-populated creation timestamp
  - `updatedAt` (LocalDateTime): Auto-populated last modified timestamp
- **Annotations**: `@Document`, `@Data`, `@Builder`, Lombok annotations

## ID Generators Created

### 1. PartyTitleIdGenerator
**File**: `src/main/java/org/fp/stamcam/utils/PartyTitleIdGenerator.java`
- Generates unique IDs with format `PRT` followed by 4 digits
- Methods:
  - `generatePartyTitleId()`: Generates next unique ID
  - `isValidPartyTitleId(String id)`: Validates ID format
  - `extractNumber(String id)`: Extracts numeric part from ID
- Thread-safe using `AtomicLong`

### 2. DeedTitleIdGenerator
**File**: `src/main/java/org/fp/stamcam/utils/DeedTitleIdGenerator.java`
- Generates unique IDs with format `DDTL` followed by 4 digits
- Methods:
  - `generateDeedTitleId()`: Generates next unique ID
  - `isValidDeedTitleId(String id)`: Validates ID format
  - `extractNumber(String id)`: Extracts numeric part from ID
- Thread-safe using `AtomicLong`

## Repositories Created

### 1. PartyTitleRepository
**File**: `src/main/java/org/fp/stamcam/repositories/PartyTitleRepository.java`
- Extends `MongoRepository<PartyTitle, String>`
- Methods:
  - `findByTitleContainingIgnoreCase(String title)`: Case-insensitive search
  - `findByTitle(String title)`: Exact title match

### 2. DeedTitleRepository
**File**: `src/main/java/org/fp/stamcam/repositories/DeedTitleRepository.java`
- Extends `MongoRepository<DeedTitle, String>`
- Methods:
  - `findByTitleContainingIgnoreCase(String title)`: Case-insensitive search
  - `findByTitle(String title)`: Exact title match

## Services Created

### 1. PartyTitleService
**File**: `src/main/java/org/fp/stamcam/services/PartyTitleService.java`
- CRUD operations for PartyTitle entities
- Methods:
  - `createPartyTitle(String title)`: Create new party title
  - `getPartyTitleById(String id)`: Retrieve by ID
  - `getAllPartyTitles()`: Get all party titles
  - `getPartyTitlesByTitle(String title)`: Search by title
  - `getPartyTitleByExactTitle(String title)`: Exact title match
  - `updatePartyTitle(String id, String title)`: Update party title
  - `deletePartyTitle(String id)`: Delete party title

### 2. DeedTitleService
**File**: `src/main/java/org/fp/stamcam/services/DeedTitleService.java`
- CRUD operations for DeedTitle entities
- Party Title management methods
- Key Methods:
  - `createDeedTitle(String title)`: Create new deed title
  - `getDeedTitleById(String id)`: Retrieve by ID
  - `getAllDeedTitles()`: Get all deed titles
  - `getDeedTitlesByTitle(String title)`: Search by title
  - `updateDeedTitle(String id, String title)`: Update deed title
  - `deleteDeedTitle(String id)`: Delete deed title
  - **`addPartyTitle(String deedTitleId, String partyTitleId)`**: Add party title to deed (prevents duplicates)
  - **`removePartyTitle(String deedTitleId, String partyTitleId)`**: Remove party title from deed
  - **`updatePartyTitleInDeed(String deedTitleId, String partyTitleId, String newTitle)`**: Update party title within deed
  - **`getPartyTitlesForDeed(String deedTitleId)`**: Get all party titles for a deed

## REST Controllers Created

### 1. PartyTitleController
**File**: `src/main/java/org/fp/stamcam/controllers/PartyTitleController.java`
- Base URL: `/api/party-titles`
- Endpoints:
  - `GET /api/party-titles` - Get all party titles
  - `GET /api/party-titles/{id}` - Get party title by ID
  - `GET /api/party-titles/search?title=...` - Search party titles
  - `POST /api/party-titles?title=...` - Create new party title
  - `PUT /api/party-titles/{id}?title=...` - Update party title
  - `DELETE /api/party-titles/{id}` - Delete party title

### 2. DeedTitleController
**File**: `src/main/java/org/fp/stamcam/controllers/DeedTitleController.java`
- Base URL: `/api/deed-titles`
- Endpoints:
  - `GET /api/deed-titles` - Get all deed titles
  - `GET /api/deed-titles/{id}` - Get deed title by ID
  - `GET /api/deed-titles/search?title=...` - Search deed titles
  - `POST /api/deed-titles?title=...` - Create new deed title
  - `PUT /api/deed-titles/{id}?title=...` - Update deed title
  - `DELETE /api/deed-titles/{id}` - Delete deed title
  - **`POST /api/deed-titles/{deedTitleId}/party-titles/{partyTitleId}`** - Add party title to deed
  - **`DELETE /api/deed-titles/{deedTitleId}/party-titles/{partyTitleId}`** - Remove party title from deed
  - **`PUT /api/deed-titles/{deedTitleId}/party-titles/{partyTitleId}?newTitle=...`** - Update party title in deed
  - **`GET /api/deed-titles/{deedTitleId}/party-titles`** - Get all party titles for deed

## Features Implemented

### 1. Auto-Generated IDs
- PartyTitle IDs: `PRT0001`, `PRT0002`, ... (4 digits)
- DeedTitle IDs: `DDTL0001`, `DDTL0002`, ... (4 digits)
- Synchronized ID generation prevents duplicates
- Max number extracted from existing documents on startup

### 2. Party Title Management in DeedTitle
- ✅ **Add Party Title**: Add existing party titles to a deed title
- ✅ **Remove Party Title**: Remove party titles from a deed title
- ✅ **Update Party Title**: Update a party title within a deed title
- ✅ **List Party Titles**: Get all party titles for a specific deed title
- Prevents duplicate party titles in a deed

### 3. MongoDB Integration
- Full Spring Data MongoDB support
- Custom query methods with regex search
- Case-insensitive title searches
- Timestamp tracking (createdAt, updatedAt)

### 4. API Documentation
- Swagger/OpenAPI annotations on all endpoints
- Comprehensive operation and response descriptions
- Available at `http://localhost:8080/api/v1/swagger-ui.html`

## Database Collections
The following MongoDB collections will be created:
- `partyTitles` - Stores all party title definitions
- `deedTitles` - Stores all deed title templates with embedded party titles

## Usage Examples

### Create a Party Title
```bash
POST /api/party-titles?title=Seller
Response: { "id": "PRT0001", "title": "Seller", ... }
```

### Create a Deed Title
```bash
POST /api/deed-titles?title=Sale%20Deed
Response: { "id": "DDTL0001", "title": "Sale Deed", "partyTitles": [], ... }
```

### Add Party Title to Deed
```bash
POST /api/deed-titles/DDTL0001/party-titles/PRT0001
Response: { "id": "DDTL0001", "title": "Sale Deed", "partyTitles": [{ "id": "PRT0001", "title": "Seller" }], ... }
```

### Remove Party Title from Deed
```bash
DELETE /api/deed-titles/DDTL0001/party-titles/PRT0001
Response: { "id": "DDTL0001", "title": "Sale Deed", "partyTitles": [], ... }
```

## Compilation Status
✅ All classes compile successfully (10 files created)
✅ No compilation errors
✅ IDE warnings are only about unused code (expected for new implementations)

## Next Steps (Optional)
1. Create test classes in `src/test/java/org/fp/stamcam/`
2. Add exception handling (e.g., `PartyTitleNotFoundException`, `DeedTitleNotFoundException`)
3. Create DTOs for API requests/responses
4. Add pagination support to list endpoints
5. Add caching for frequently accessed titles

