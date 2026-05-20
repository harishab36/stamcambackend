# Deed Entity - Comprehensive Documentation

## Overview

The **Deed** entity represents a legal deed document in the StamCam system. It manages important legal documents with unique identifiers, categorization by deed type, and full document content storage.

## Entity Structure

### Deed Class
Located at: `src/main/java/org/fp/stamcam/models/Deed.java`

```java
@Document(collection = "deeds")
public class Deed {
    @Id
    private String id;              // Format: DD + 8 digits (e.g., DD00000001)
    private String title;           // Title of the deed
    private String matter;          // Full content/matter as text blob
    private DeedType type;          // Enumeration of deed types
    private LocalDateTime createdAt;  // Creation timestamp
    private LocalDateTime updatedAt;  // Last update timestamp
}
```

### Field Descriptions

| Field | Type | Description | Format/Constraints |
|-------|------|-------------|-------------------|
| **id** | String | Unique identifier | DD + 8 digits (e.g., DD00000001) |
| **title** | String | Deed title/name | Any text |
| **matter** | String | Legal document content | Text blob (large text content) |
| **type** | DeedType Enum | Category of deed | See DeedType section below |
| **createdAt** | LocalDateTime | Creation timestamp | Auto-generated on creation |
| **updatedAt** | LocalDateTime | Last modification timestamp | Auto-updated on each modification |

---

## ID Generation Format

### Format: DD + 8 Digits

- **Prefix**: `DD` (Deed Document)
- **Number**: 8-digit sequential number (00000001 to 99999999)
- **Examples**: DD00000001, DD00000002, DD00000100

### Auto-Generation
- IDs are **automatically generated** when creating a new deed using the `DeedIdGenerator` utility
- The system ensures uniqueness by tracking the highest existing number and incrementing
- Manual ID assignment is not recommended - the system will override it

---

## DeedType Enumeration

Located at: `src/main/java/org/fp/stamcam/models/DeedType.java`

### Available Deed Types

| Type | Display Name | Description |
|------|--------------|-------------|
| `SALE_DEED` | Sale Deed | Property sale transfer |
| `GIFT_DEED` | Gift Deed | Property given as a gift |
| `MORTGAGE_DEED` | Mortgage Deed | Property mortgaged |
| `LEASE_DEED` | Lease Deed | Property leased |
| `EXCHANGE_DEED` | Exchange Deed | Property exchanged |
| `PARTITION_DEED` | Partition Deed | Property partition |
| `DONATION_DEED` | Donation Deed | Property donation |
| `RELEASE_DEED` | Release Deed | Release of rights |
| `TRANSFER_DEED` | Transfer Deed | General property transfer |
| `AFFIDAVIT_DEED` | Affidavit Deed | Affidavit-based transfer |

---

## REST API Endpoints

### Base URL
```
http://localhost:8080/stamcam/api/deeds
```

### CRUD Operations

#### 1. Create Deed
```
POST /api/deeds
Content-Type: application/json

{
    "title": "Property Sales Agreement",
    "matter": "This deed documents the sale of property...",
    "type": "SALE_DEED"
}

Response (201 Created):
{
    "id": "DD00000001",
    "title": "Property Sales Agreement",
    "matter": "This deed documents the sale of property...",
    "type": "SALE_DEED",
    "createdAt": "2026-05-15T10:30:00",
    "updatedAt": "2026-05-15T10:30:00"
}
```

#### 2. Get All Deeds
```
GET /api/deeds

Response (200 OK):
[
    {
        "id": "DD00000001",
        "title": "Property Sales Agreement",
        "type": "SALE_DEED",
        ...
    },
    {
        "id": "DD00000002",
        "title": "Property Gift Deed",
        "type": "GIFT_DEED",
        ...
    }
]
```

#### 3. Get Deed by ID
```
GET /api/deeds/{id}
Example: GET /api/deeds/DD00000001

Response (200 OK):
{
    "id": "DD00000001",
    "title": "Property Sales Agreement",
    "matter": "..full content...",
    "type": "SALE_DEED",
    "createdAt": "2026-05-15T10:30:00",
    "updatedAt": "2026-05-15T10:30:00"
}
```

#### 4. Update Deed
```
PUT /api/deeds/{id}
Example: PUT /api/deeds/DD00000001
Content-Type: application/json

{
    "title": "Updated Property Sales Agreement",
    "matter": "Updated content...",
    "type": "SALE_DEED"
}

Response (200 OK):
{
    "id": "DD00000001",
    "title": "Updated Property Sales Agreement",
    ...
}
```

#### 5. Delete Deed
```
DELETE /api/deeds/{id}
Example: DELETE /api/deeds/DD00000001

Response (204 No Content)
```

### Advanced Search & Filtering

#### 6. Get Deeds by Type
```
GET /api/deeds/type/{type}
Example: GET /api/deeds/type/SALE_DEED

Response (200 OK):
[
    { "id": "DD00000001", "type": "SALE_DEED", ... },
    { "id": "DD00000003", "type": "SALE_DEED", ... }
]
```

#### 7. Search Deeds by Title (Case-Insensitive)
```
GET /api/deeds/search/title/{title}
Example: GET /api/deeds/search/title/Property

Response (200 OK):
[
    { "id": "DD00000001", "title": "Property Sales Agreement", ... },
    { "id": "DD00000002", "title": "Property Gift Deed", ... }
]
```

#### 8. Get Deed by Exact Title
```
GET /api/deeds/exact/title/{title}
Example: GET /api/deeds/exact/title/Property%20Sales%20Agreement

Response (200 OK):
{
    "id": "DD00000001",
    "title": "Property Sales Agreement",
    ...
}
```

#### 9. Filter by Type and Title
```
GET /api/deeds/filter/type/{type}/title/{title}
Example: GET /api/deeds/filter/type/SALE_DEED/title/Property

Response (200 OK):
[
    { "id": "DD00000001", "title": "Property Sales Agreement", "type": "SALE_DEED", ... }
]
```

---

## Code Components

### Repository: DeedRepository
Located at: `src/main/java/org/fp/stamcam/repositories/DeedRepository.java`

Extends `MongoRepository<Deed, String>` with custom methods:
- `findByType(DeedType type)` - Find deeds by type
- `findByTitleContainingIgnoreCase(String title)` - Search by title
- `findByTitle(String title)` - Find by exact title
- `findByTypeAndTitleContainingIgnoreCase(DeedType type, String title)` - Combined filter

### Service: DeedService
Located at: `src/main/java/org/fp/stamcam/services/DeedService.java`

Business logic methods:
- `getAllDeeds()` - Retrieve all deeds
- `getDeedById(String id)` - Get specific deed
- `createDeed(Deed deed)` - Create with auto-generated ID
- `updateDeed(String id, Deed deed)` - Update deed
- `deleteDeed(String id)` - Delete deed
- `getDeedsByType(DeedType type)` - Filter by type
- `searchDeedsByTitle(String title)` - Search by title
- `getDeedByTitle(String title)` - Get exact title match
- `getDeedsByTypeAndTitle(DeedType type, String title)` - Combined filter

### Controller: DeedController
Located at: `src/main/java/org/fp/stamcam/controllers/DeedController.java`

Maps HTTP requests to service methods. All endpoints include Swagger documentation.

### Utility: DeedIdGenerator
Located at: `src/main/java/org/fp/stamcam/utils/DeedIdGenerator.java`

Handles ID generation and validation:
- `generateDeedId()` - Generate unique ID with format DD + 8 digits
- `isValidDeedId(String id)` - Validate ID format
- `extractNumber(String id)` - Extract numeric part from ID

---

## cURL Examples

### Create a Deed
```bash
curl -X POST http://localhost:8080/stamcam/api/deeds \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Property Transfer for 123 Main Street",
    "matter": "This legal deed documents the transfer of the property located at 123 Main Street, City, State from the seller to the buyer...",
    "type": "SALE_DEED"
  }'
```

### Get All Deeds
```bash
curl http://localhost:8080/stamcam/api/deeds
```

### Get Deed by ID
```bash
curl http://localhost:8080/stamcam/api/deeds/DD00000001
```

### Search by Title
```bash
curl http://localhost:8080/stamcam/api/deeds/search/title/Property
```

### Get Deeds by Type
```bash
curl http://localhost:8080/stamcam/api/deeds/type/SALE_DEED
```

### Update Deed
```bash
curl -X PUT http://localhost:8080/stamcam/api/deeds/DD00000001 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Transfer Title",
    "matter": "Updated document content...",
    "type": "SALE_DEED"
  }'
```

### Delete Deed
```bash
curl -X DELETE http://localhost:8080/stamcam/api/deeds/DD00000001
```

---

## Unit Testing

### DeedServiceTest
Located at: `src/test/java/org/fp/stamcam/services/DeedServiceTest.java`

Test methods (8 total):
- `testGetAllDeeds()` - Test retrieval of all deeds
- `testGetDeedById()` - Test ID-based retrieval
- `testCreateDeed()` - Test ID generation and creation
- `testUpdateDeed()` - Test partial updates
- `testDeleteDeed()` - Test deletion
- `testGetDeedsByType()` - Test type-based filtering
- `testSearchDeedsByTitle()` - Test title search
- `testGetDeedByTitle()` - Test exact title retrieval
- `testGetDeedsByTypeAndTitle()` - Test combined filtering

### Run Tests
```bash
# All Deed tests
mvn test -Dtest=DeedServiceTest

# All tests including Deeds
mvn test
```

---

## Database Configuration

### MongoDB Collection
- **Collection Name**: `deeds`
- **Database**: `stamcam_db`
- **URI**: `mongodb://localhost:27017/stamcam_db`

### ID Index
Auto-indexed by MongoDB on the `_id` field. Type and title fields can be manually indexed for performance:

```javascript
// Optional: MongoDB indexes for optimization
db.deeds.createIndex({ "type": 1 })
db.deeds.createIndex({ "title": "text" })
db.deeds.createIndex({ "type": 1, "title": 1 })
```

---

## Swagger/API Documentation

View interactive API documentation:
```
http://localhost:8080/stamcam/api/v1/swagger-ui.html
```

All endpoints are fully documented with:
- Operation descriptions
- Parameter specifications
- Request/response examples
- Error response codes

---

## Design Patterns Used

1. **Auto-ID Generation** - Automatic sequential ID with custom format
2. **Enumeration Pattern** - DeedType for type-safe deed categorization
3. **Repository Pattern** - MongoDB abstraction via Spring Data
4. **Service Layer Pattern** - Business logic isolation
5. **REST API Pattern** - Standard HTTP methods (GET, POST, PUT, DELETE)
6. **Timestamp Pattern** - createdAt/updatedAt for audit trail

---

## Future Enhancements

Potential extensions to the Deed entity:
- Add `signingParties` (list of parties involved)
- Add `registrationNumber` (official deed registration number)
- Add `registrationDate` (when deed was officially registered)
- Add `propertyDetails` (embedded document with property info)
- Add file/document storage (PDF, image attachments)
- Add workflow/approval status tracking
- Add versioning for tracking changes
- Add full-text search on `matter` field
- Add encryption for sensitive document content

---

**Deed Entity is Production-Ready! 🎉**

