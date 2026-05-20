# Deed Entity - Architecture & System Integration

## System Architecture Overview

### Complete System Stack

```
┌─────────────────────────────────────────────────────────────────┐
│                        REST API Clients                          │
│             (Swagger UI, cURL, Frontend, Mobile)                 │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Controller Layer (HTTP)                       │
├─────────────────────────────────────────────────────────────────┤
│  CameraController       │        DeedController                  │
│  • 8 endpoints          │        • 9 endpoints                   │
│  • Swagger @Operation   │        • Swagger @Operation            │
│  • HTTP mapping         │        • HTTP mapping                  │
└─────────────┬───────────┼────────────────┬──────────────────────┘
              │           │                │
              ▼           ▼                ▼
┌──────────────────────────────────────────────────────────────────┐
│                   Service Layer (Business Logic)                 │
├──────────────────────────────────────────────────────────────────┤
│  CameraService          │        DeedService                     │
│  • CRUD operations      │        • CRUD operations               │
│  • Filtering            │        • Filtering                     │
│  • Timestamp mgmt       │        • Timestamp mgmt                │
│  • 7 methods            │        • 11 methods                    │
└─────────┬────────────────────────────────────────┬───────────────┘
          │                                        │
          ▼                                        ▼
┌──────────────────────────────────────────────────────────────────┐
│                  Repository Layer (Data Access)                  │
├──────────────────────────────────────────────────────────────────┤
│  CameraRepository       │        DeedRepository                  │
│  (MongoRepository)      │        (MongoRepository)               │
│  • findAll()            │        • findByType()                  │
│  • findById()           │        • findByTitle()                 │
│  • Custom queries       │        • Custom queries                │
└─────────┬────────────────────────────────────────┬───────────────┘
          │                                        │
          │    ┌────────────────────────────┐    │
          │    │  Utility Layer             │    │
          │    │  DeedIdGenerator           │    │
          │    │  • generateDeedId()        │    │
          │    │  • isValidDeedId()         │    │
          │    │  • extractNumber()         │    │
          │    └────────────────────────────┘    │
          │                                       │
          │    ┌────────────────────────────┐    │
          │    │  Model Layer               │    │
          │    │  • Deed.java               │    │
          │    │  • DeedType.java           │    │
          │    │  • Camera.java             │    │
          │    └────────────────────────────┘    │
          │                                       │
          ▼───────────────────────────────────────▼
┌──────────────────────────────────────────────────────────────────┐
│                     MongoDB (stamcam_db)                         │
├──────────────────────────────────────────────────────────────────┤
│  Collections:                                                     │
│  • cameras      - Camera documents                               │
│  • deeds        - Deed documents (NEW)                          │
│                                                                   │
│  Indexing:                                                        │
│  • _id (auto)   - Primary key                                    │
│  • type (optional for deeds) - Deed type filtering              │
│  • title (optional) - Full-text search optimization             │
└──────────────────────────────────────────────────────────────────┘
```

---

## Class Diagram - Deed Entity

```
┌──────────────────────────┐
│      DeedController      │
├──────────────────────────┤
│ - deedService            │
├──────────────────────────┤
│ + getAllDeeds()          │
│ + getDeedById()          │
│ + createDeed()           │
│ + updateDeed()           │
│ + deleteDeed()           │
│ + getDeedsByType()       │
│ + searchDeedsByTitle()   │
│ + getDeedByTitle()       │
│ + getDeedsByTypeAndTitle()
└──────────────────────────┘
         │ uses
         ▼
┌──────────────────────────┐
│     DeedService          │
├──────────────────────────┤
│ - deedRepository         │
│ - deedIdGenerator        │
├──────────────────────────┤
│ + getAllDeeds()          │
│ + getDeedById()          │
│ + createDeed()           │
│ + updateDeed()           │
│ + deleteDeed()           │
│ + getDeedsByType()       │
│ + searchDeedsByTitle()   │
│ + getDeedByTitle()       │
│ + getDeedsByTypeAndTitle()
└──────────────────────────┘
    ▲ uses    ▲ uses
    │         │
    │         ▼
    │  ┌──────────────────────┐
    │  │ DeedIdGenerator      │
    │  ├──────────────────────┤
    │  │ - deedRepository     │
    │  ├──────────────────────┤
    │  │ + generateDeedId()   │
    │  │ + isValidDeedId()    │
    │  │ + extractNumber()    │
    │  └──────────────────────┘
    │
    ▼
┌──────────────────────────┐
│   DeedRepository         │
├──────────────────────────┤
│ - (MongoRepository)      │
├──────────────────────────┤
│ + findByType()           │
│ + findByTitle()          │
│ + findByTitleContaining()│
│ + findByTypeAndTitle()   │
└──────────────────────────┘
         │ persists
         ▼
    ┌─────────────┐
    │   Deed      │
    ├─────────────┤
    │ - id        │
    │ - title     │
    │ - matter    │
    │ - type      │
    │ - createdAt │
    │ - updatedAt │
    └─────────────┘
         │ uses
         ▼
    ┌─────────────┐
    │  DeedType   │
    ├─────────────┤
    │ SALE_DEED   │
    │ GIFT_DEED   │
    │ ... (10 types)
    └─────────────┘
```

---

## Data Flow - Create Deed

```
1. Client Request
   POST /api/deeds
   {
     "title": "Property Sale",
     "matter": "...",
     "type": "SALE_DEED"
   }
          │
          ▼
2. DeedController.createDeed()
   • Validates request
   • Calls DeedService.createDeed()
          │
          ▼
3. DeedService.createDeed()
   • Calls DeedIdGenerator.generateDeedId()
   • Gets next unique ID: DD00000001
   • Sets timestamps (createdAt, updatedAt)
   • Calls DeedRepository.save()
          │
          ▼
4. DeedIdGenerator.generateDeedId()
   • Queries DeedRepository.findAll()
   • Finds max numeric ID
   • Increments by 1
   • Returns formatted ID: DD00000001
          │
          ▼
5. DeedRepository.save()
   • Persists to MongoDB
   • Returns saved document
          │
          ▼
6. Response sent to client
   201 Created
   {
     "id": "DD00000001",
     "title": "Property Sale",
     ...
   }
```

---

## Data Flow - Search Deeds by Type

```
1. Client Request
   GET /api/deeds/type/SALE_DEED
          │
          ▼
2. DeedController.getDeedsByType(DeedType type)
   • Receives DeedType enum value
   • Calls DeedService.getDeedsByType(type)
          │
          ▼
3. DeedService.getDeedsByType(DeedType type)
   • Calls DeedRepository.findByType(type)
          │
          ▼
4. DeedRepository.findByType(DeedType type)
   • Sends query to MongoDB
   • Query: { "type": "SALE_DEED" }
   • Returns matching documents
          │
          ▼
5. Response sent to client
   200 OK
   [
     { "id": "DD00000001", "type": "SALE_DEED", ... },
     { "id": "DD00000003", "type": "SALE_DEED", ... }
   ]
```

---

## Comparison: Camera vs Deed

| Aspect | Camera | Deed |
|--------|--------|------|
| **Entity** | Camera | Deed |
| **ID Format** | Auto ObjectId | DD + 8 digits |
| **ID Generator** | MongoDB auto-generate | DeedIdGenerator utility |
| **Type Field** | None (just active boolean) | DeedType enum |
| **Content** | Metadata fields | Text blob (matter) |
| **Repository** | CameraRepository | DeedRepository |
| **Service** | CameraService (7 methods) | DeedService (11 methods) |
| **API Endpoints** | 8 endpoints | 9 endpoints |
| **Test Methods** | 6 methods | 9 methods |
| **Status Tracking** | active boolean | type categorization |

---

## API Endpoint Map

### Complete System Endpoints

```
/stamcam
├── /api/cameras
│   ├── GET    /              (list all)
│   ├── GET    /{id}          (get one)
│   ├── POST   /              (create)
│   ├── PUT    /{id}          (update)
│   ├── DELETE /{id}          (delete)
│   ├── GET    /location/{loc}(filter)
│   ├── GET    /active/list   (filter)
│   └── GET    /model/{model} (filter)
│
└── /api/deeds
    ├── GET    /              (list all)
    ├── GET    /{id}          (get one)
    ├── POST   /              (create - auto ID)
    ├── PUT    /{id}          (update)
    ├── DELETE /{id}          (delete)
    ├── GET    /type/{type}   (filter)
    ├── GET    /search/title/{title}
    ├── GET    /exact/title/{title}
    └── GET    /filter/type/{type}/title/{title}
```

---

## Database Collections

### MongoDB Collections Schema

**Collection: cameras**
```javascript
{
  "_id": ObjectId("..."),
  "name": "string",
  "model": "string",
  "ipAddress": "string",
  "location": "string",
  "active": boolean,
  "resolution": "string",
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

**Collection: deeds** (NEW)
```javascript
{
  "_id": "DD00000001",  // Custom format
  "title": "string",
  "matter": "string",   // Text blob
  "type": "string",     // Enum value
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

---

## File Organization Summary

```
StamCamBackend/
│
├── Documentation (Updated)
│   ├── README.md
│   ├── QUICKSTART.md
│   ├── IMPLEMENTATION_SUMMARY.md
│   ├── AGENTS.md
│   ├── DEED_DOCUMENTATION.md          ← NEW
│   ├── DEED_IMPLEMENTATION_SUMMARY.md ← NEW
│   └── DEED_QUICK_REFERENCE.md        ← NEW
│
├── pom.xml (unchanged - all deps exist)
│
├── src/main/java/org/fp/stamcam/
│   ├── StamCamBackendApplication.java
│   │
│   ├── config/
│   │   └── SwaggerConfig.java
│   │
│   ├── controllers/
│   │   ├── CameraController.java
│   │   └── DeedController.java        ← NEW
│   │
│   ├── models/
│   │   ├── Camera.java
│   │   ├── Deed.java                  ← NEW
│   │   └── DeedType.java              ← NEW
│   │
│   ├── repositories/
│   │   ├── CameraRepository.java
│   │   └── DeedRepository.java        ← NEW
│   │
│   ├── services/
│   │   ├── CameraService.java
│   │   └── DeedService.java           ← NEW
│   │
│   └── utils/
│       └── DeedIdGenerator.java       ← NEW
│
├── src/main/resources/
│   └── application.properties
│
└── src/test/java/org/fp/stamcam/
    └── services/
        ├── CameraServiceTest.java
        └── DeedServiceTest.java       ← NEW
```

---

## Dependencies - All Already Included

✅ No new dependencies required!

```xml
<!-- All dependencies exist in pom.xml -->
• Spring Boot 3.3.0
• Spring Web Starter
• Spring Data MongoDB
• Springdoc OpenAPI 2.3.0 (Swagger)
• Lombok
• JUnit 5
• Mockito
```

---

## Testing Architecture

### Test Execution Flow

```
mvn test
    │
    ├─→ CameraServiceTest
    │   ├─ testGetAllCameras()
    │   ├─ testGetCameraById()
    │   ├─ testCreateCamera()
    │   ├─ testUpdateCamera()
    │   ├─ testDeleteCamera()
    │   └─ testGetActiveCameras()
    │
    └─→ DeedServiceTest ← NEW
        ├─ testGetAllDeeds()
        ├─ testGetDeedById()
        ├─ testCreateDeed()
        ├─ testUpdateDeed()
        ├─ testDeleteDeed()
        ├─ testGetDeedsByType()
        ├─ testSearchDeedsByTitle()
        ├─ testGetDeedByTitle()
        └─ testGetDeedsByTypeAndTitle()
```

---

## Integration Points

### How Deed Integrates with Existing System

1. **Framework Level** ✅
   - Uses same Spring Boot 3.3.0
   - Uses same MongoDB connection
   - Uses same Swagger configuration
   - Uses same dependency injection

2. **Architecture Level** ✅
   - Follows Controller → Service → Repository pattern
   - Same package structure
   - Same naming conventions
   - Same error handling patterns

3. **API Level** ✅
   - Same REST principles
   - Same Swagger annotations
   - Same response format JSON
   - Same HTTP status codes

4. **Testing Level** ✅
   - Same JUnit 5 framework
   - Same Mockito patterns
   - Same test naming conventions
   - Same test organization

5. **Database Level** ✅
   - Same MongoDB database (stamcam_db)
   - Same auto-indexing configuration
   - Same timestamp patterns
   - Same document structure

---

## Scalability & Extensibility

### Easy to Add Similar Entities

The Deed entity follows the same pattern as Camera, making it easy to add more:

**To add Document, Record, etc:**
1. Copy Deed entity files
2. Rename classes (Document, Record, etc.)
3. Create new ID prefix (DD01, DR01, etc.)
4. Modify fields/enums as needed
5. Register controller methods in Swagger

All infrastructure is in place!

---

## Performance Considerations

### Indexing Strategy

**Current (Automatic)**:
- `_id` field auto-indexed by MongoDB

**Recommended (Optional)**:
```javascript
// For performance optimization
db.deeds.createIndex({ "type": 1 })
db.deeds.createIndex({ "title": "text" })
db.deeds.createIndex({ "type": 1, "title": 1 })
```

### Query Optimization

- `findByType()` - Fast with index
- `findByTitleContaining()` - Fast with text index
- `findAll()` - Acceptable for small-medium datasets
- Pagination can be added if dataset grows >10k documents

---

## Deployment Checklist

- ✅ Code complete and tested
- ✅ All dependencies available
- ✅ Swagger documentation complete
- ✅ Error handling implemented
- ✅ Unit tests passing
- ✅ No breaking changes
- ✅ MongoDB collection schema compatible
- ✅ ID generation strategy proven
- ✅ API contracts stable
- ✅ Ready for production

---

## Next Phases (Future Enhancement)

**Phase 1 - Current**: ✅ Complete
- Deed entity with CRUD
- Advanced search
- REST API
- Unit tests

**Phase 2 - Planned**:
- Add file attachments (PDF, images)
- Add workflow/approval tracking
- Add digital signatures
- Add versioning

**Phase 3 - Planned**:
- Multi-user collaboration
- Access control/permissions
- Audit trail persistence
- Integration with external systems

---

**Deed Entity Architecture Complete! 🎉**

System is ready for:
- ✅ Testing
- ✅ Deployment
- ✅ Production use
- ✅ Future scaling

