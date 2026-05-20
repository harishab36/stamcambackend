# Deed Entity - Quick Reference Guide

## 📋 Entity Overview

**Purpose**: Manage legal deed documents with unique identifiers and categorization.

**Key Features**:
- ✅ Auto-generated ID (format: DD + 8 digits)
- ✅ Enumerated deed types (10 types available)
- ✅ Full document content storage (matter as text blob)
- ✅ Complete CRUD API (9 endpoints)
- ✅ Advanced search and filtering
- ✅ Timestamp auditing
- ✅ Fully tested

---

## 🏗️ Files Created

| File | Type | Purpose |
|------|------|---------|
| `DeedType.java` | Enum | 10 deed type categories |
| `Deed.java` | Model | MongoDB document entity |
| `DeedRepository.java` | Repository | Data access layer |
| `DeedIdGenerator.java` | Utility | Auto-ID generation |
| `DeedService.java` | Service | Business logic (11 methods) |
| `DeedController.java` | Controller | REST endpoints (9 endpoints) |
| `DeedServiceTest.java` | Test | Unit tests (9 test methods) |

---

## 🎯 ID Format

| Component | Format | Example |
|-----------|--------|---------|
| Prefix | "DD" | DD |
| Number | 8 digits | 00000001 |
| Full ID | DD + 8 digits | DD00000001 |

**Auto-generated incrementally**: DD00000001 → DD00000002 → DD00000003...

---

## 📚 Deed Types

```
SALE_DEED        - Property sale transfer
GIFT_DEED        - Property given as a gift
MORTGAGE_DEED    - Property mortgaged
LEASE_DEED       - Property leased
EXCHANGE_DEED    - Property exchanged
PARTITION_DEED   - Property partition
DONATION_DEED    - Property donation
RELEASE_DEED     - Release of rights
TRANSFER_DEED    - General property transfer
AFFIDAVIT_DEED   - Affidavit-based transfer
```

---

## 🔌 REST API Endpoints

### Base URL
```
http://localhost:8080/stamcam/api/deeds
```

### Endpoints Summary

| Method | Endpoint | Purpose | ID? |
|--------|----------|---------|-----|
| POST | `/deeds` | Create deed | Auto ✓ |
| GET | `/deeds` | Get all | N/A |
| GET | `/deeds/{id}` | Get one | Yes |
| PUT | `/deeds/{id}` | Update | Yes |
| DELETE | `/deeds/{id}` | Delete | Yes |
| GET | `/deeds/type/{type}` | Filter by type | N/A |
| GET | `/deeds/search/title/{title}` | Search title | N/A |
| GET | `/deeds/exact/title/{title}` | Exact title | N/A |
| GET | `/deeds/filter/type/{type}/title/{title}` | Type + title | N/A |

---

## 💻 API Usage Examples

### 1. Create Deed (ID Auto-Generated)
```bash
curl -X POST http://localhost:8080/stamcam/api/deeds \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Main Street Property Sale",
    "matter": "This deed documents the sale...",
    "type": "SALE_DEED"
  }'
```

**Response**:
```json
{
  "id": "DD00000001",
  "title": "Main Street Property Sale",
  "matter": "This deed documents the sale...",
  "type": "SALE_DEED",
  "createdAt": "2026-05-15T10:30:00",
  "updatedAt": "2026-05-15T10:30:00"
}
```

### 2. Get All Deeds
```bash
curl http://localhost:8080/stamcam/api/deeds
```

### 3. Get by ID
```bash
curl http://localhost:8080/stamcam/api/deeds/DD00000001
```

### 4. Filter by Type
```bash
curl http://localhost:8080/stamcam/api/deeds/type/SALE_DEED
```

### 5. Search by Title
```bash
curl http://localhost:8080/stamcam/api/deeds/search/title/Property
```

### 6. Update Deed
```bash
curl -X PUT http://localhost:8080/stamcam/api/deeds/DD00000001 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Title",
    "type": "GIFT_DEED"
  }'
```

### 7. Delete Deed
```bash
curl -X DELETE http://localhost:8080/stamcam/api/deeds/DD00000001
```

---

## 🗂️ Package Structure

```
src/main/java/org/fp/stamcam/
├── models/
│   ├── Deed.java           ← Entity
│   └── DeedType.java       ← Enum
├── repositories/
│   └── DeedRepository.java ← Data access
├── services/
│   └── DeedService.java    ← Business logic
├── controllers/
│   └── DeedController.java ← REST API
└── utils/
    └── DeedIdGenerator.java ← ID generation

src/test/java/org/fp/stamcam/services/
└── DeedServiceTest.java    ← Unit tests
```

---

## 🧪 Testing

### Run Deed Tests
```bash
mvn test -Dtest=DeedServiceTest
```

### Test Methods Available
- ✅ testGetAllDeeds()
- ✅ testGetDeedById()
- ✅ testCreateDeed()
- ✅ testUpdateDeed()
- ✅ testDeleteDeed()
- ✅ testGetDeedsByType()
- ✅ testSearchDeedsByTitle()
- ✅ testGetDeedByTitle()
- ✅ testGetDeedsByTypeAndTitle()

---

## 🔍 Database

### MongoDB Collection
- **Collection**: `deeds`
- **Database**: `stamcam_db`
- **Document Count**: Grows with each deed creation

### Sample Document
```json
{
  "_id": "DD00000001",
  "title": "Property Sale Agreement",
  "matter": "This legal document...",
  "type": "SALE_DEED",
  "createdAt": ISODate("2026-05-15T10:30:00Z"),
  "updatedAt": ISODate("2026-05-15T10:30:00Z")
}
```

---

## 📊 Entity Fields

| Field | Type | Auto? | Required | Description |
|-------|------|-------|----------|-------------|
| id | String | ✓ Yes | Yes | DD + 8 digits |
| title | String | ✗ No | Yes | Deed title |
| matter | String | ✗ No | Yes | Document content |
| type | DeedType | ✗ No | Yes | Deed category |
| createdAt | DateTime | ✓ Yes | Auto | Creation time |
| updatedAt | DateTime | ✓ Yes | Auto | Update time |

---

## 🚀 Quick Start

### 1. Compile
```bash
mvn clean compile
```

### 2. Run Tests
```bash
mvn test -Dtest=DeedServiceTest
```

### 3. Start Application
```bash
mvn spring-boot:run
```

### 4. Access Swagger UI
```
http://localhost:8080/stamcam/api/v1/swagger-ui.html
```

### 5. Test Deed Endpoints
- Use Swagger UI or cURL examples above

---

## 🔐 ID Security

### Format Validation
- Pattern: `^DD\d{8}$`
- Valid: DD00000001, DD99999999
- Invalid: dd00000001, DD0001, DD-00000001

### Uniqueness
- Database enforces uniqueness on `_id` field
- Generator prevents ID collisions
- Sequential guarantee for new deeds

---

## 📝 Service Methods

```java
// CRUD Operations
getAllDeeds()                                    // Get all
getDeedById(String id)                          // Get one
createDeed(Deed deed)                           // Create (auto ID)
updateDeed(String id, Deed deed)               // Update
deleteDeed(String id)                          // Delete

// Search & Filter
getDeedsByType(DeedType type)                  // By type
searchDeedsByTitle(String title)               // Title search (partial)
getDeedByTitle(String title)                   // Title exact match
getDeedsByTypeAndTitle(DeedType type, String title)  // Combined
```

---

## 🎨 Swagger Documentation

### Tag
**Deed Management** - APIs for managing legal deed documents

### Features
- ✅ Full endpoint documentation
- ✅ Parameter descriptions with examples
- ✅ Request/response schemas
- ✅ Error code documentation
- ✅ Try-it-out functionality

### Access
```
http://localhost:8080/stamcam/api/v1/swagger-ui.html
```

---

## ⚙️ Dependencies

**No new dependencies required!** Uses existing:
- Spring Boot 3.3.0
- Spring Data MongoDB
- Springdoc OpenAPI
- Lombok
- JUnit 5
- Mockito

---

## 📖 Documentation Files

| File | Content |
|------|---------|
| `DEED_DOCUMENTATION.md` | Complete reference |
| `DEED_IMPLEMENTATION_SUMMARY.md` | Implementation details |
| `DEED_QUICK_REFERENCE.md` | This file |

---

## ✅ Production Checklist

- ✅ All CRUD operations working
- ✅ Custom ID generation tested
- ✅ All enum types available
- ✅ REST endpoints secured
- ✅ Error handling implemented
- ✅ Swagger documented
- ✅ Unit tests passing
- ✅ No breaking changes
- ✅ Ready for deployment

---

## 🆘 Common Tasks

### Create First Deed
```bash
curl -X POST http://localhost:8080/stamcam/api/deeds \
  -H "Content-Type: application/json" \
  -d '{"title":"First Deed","matter":"Content...","type":"SALE_DEED"}'
```

### List All Deeds
```bash
curl http://localhost:8080/stamcam/api/deeds | json_pp
```

### Get by ID
```bash
curl http://localhost:8080/stamcam/api/deeds/DD00000001 | json_pp
```

### Run All Tests
```bash
mvn test
```

---

**Ready to use the Deed Entity! 🎉**

For detailed information, see `DEED_DOCUMENTATION.md`

