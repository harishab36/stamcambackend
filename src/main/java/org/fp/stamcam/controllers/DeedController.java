package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.DeedType;
import org.fp.stamcam.models.DeedStatus;
import org.fp.stamcam.models.IdType;
import org.fp.stamcam.models.Party;
import org.fp.stamcam.services.DeedService;
import org.fp.stamcam.services.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for deed operations.
 * Provides endpoints for CRUD operations on deeds and advanced filtering capabilities.
 */
@RestController
@RequestMapping("/api/deeds")
@Tag(name = "Deed Management", description = "APIs for managing legal deed documents")
public class DeedController {

    @Autowired
    private DeedService deedService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    /**
     * Get all deeds.
     *
     * @return list of all deeds
     */
    @GetMapping
    @Operation(summary = "Get all deeds", description = "Retrieve a list of all deeds in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getAllDeeds() {
        return ResponseEntity.ok(deedService.getAllDeeds());
    }

    /**
     * Get a deed by ID.
     *
     * @param id the deed ID (format: DD + 8 digits, e.g., DD00000001)
     * @return the deed if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get deed by ID", description = "Retrieve a specific deed by its ID (format: DD + 8 digits)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deed found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> getDeedById(
            @Parameter(description = "Deed ID (format: DD00000001)", required = true, example = "DD00000001")
            @PathVariable String id) {
        return deedService.getDeedById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a consolidated count of deeds by status.
     *
     * @return a map containing the count for each status
     */
    @GetMapping("/count-by-status")
    @Operation(summary = "Get deed count by status", description = "Get a consolidated count of deeds for each status (DRAFT, IN_PROGRESS, COMPLETED).")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deed counts",
            content = @Content(mediaType = "application/json", schema = @Schema(type = "object", additionalProperties = Schema.AdditionalPropertiesValue.TRUE)))
    public ResponseEntity<Map<String, Long>> getDeedsCountByStatus() {
        return ResponseEntity.ok(deedService.getDeedsCountByStatus());
    }

    /**
     * Generate PDF for a deed.
     *
     * @param id the deed ID
     * @return PDF byte array
     */
    @GetMapping("/{id}/pdf")
    @Operation(summary = "Generate Deed PDF", description = "Generates a structured PDF for the deed including reconstructed party ID documents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF generated successfully",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<byte[]> generateDeedPdf(
            @Parameter(description = "Deed ID (format: DD00000001)", required = true, example = "DD00000001")
            @PathVariable String id) {
        Optional<Deed> deedOptional = deedService.getDeedById(id);
        
        if (deedOptional.isPresent()) {
            byte[] pdfBytes = pdfGenerationService.generateDeedPdf(deedOptional.get());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Suggesting a filename for download
            headers.setContentDispositionFormData("attachment", "deed_" + id + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new deed with auto-generated ID.
     *
     * @param deed the deed to create
     * @return the created deed with generated ID
     */
    @PostMapping
    @Operation(summary = "Create a new deed", description = "Create a new deed document. ID will be auto-generated with format DD + 8 digits")
    @ApiResponse(responseCode = "201", description = "Deed created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<Deed> createDeed(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Deed object to be created", required = true)
            @RequestBody Deed deed) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deedService.createDeed(deed));
    }

    /**
     * Update an existing deed.
     *
     * @param id the deed ID
     * @param deed the updated deed data
     * @return the updated deed
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a deed", description = "Update an existing deed document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deed updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> updateDeed(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated deed data", required = true)
            @RequestBody Deed deed) {
        return deedService.updateDeed(id, deed)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a deed.
     *
     * @param id the deed ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a deed", description = "Delete a deed document by its ID")
    @ApiResponse(responseCode = "204", description = "Deed deleted successfully")
    public ResponseEntity<Void> deleteDeed(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String id) {
        deedService.deleteDeed(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get deeds by type.
     *
     * @param type the deed type (SALE_DEED, GIFT_DEED, etc.)
     * @return list of deeds of the specified type
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Get deeds by type", description = "Retrieve deeds filtered by type (SALE_DEED, GIFT_DEED, MORTGAGE_DEED, etc.)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByType(
            @Parameter(description = "Deed type", required = true, example = "SALE_DEED")
            @PathVariable DeedType type) {
        return ResponseEntity.ok(deedService.getDeedsByType(type));
    }

    /**
     * Search deeds by title.
     *
     * @param title the title search term
     * @return list of deeds with matching title
     */
    @GetMapping("/search/title/{title}")
    @Operation(summary = "Search deeds by title", description = "Search deeds by title (case-insensitive partial match)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> searchDeedsByTitle(
            @Parameter(description = "Title search term", required = true, example = "Property Transfer")
            @PathVariable String title) {
        return ResponseEntity.ok(deedService.searchDeedsByTitle(title));
    }

    /**
     * Get deed by exact title match.
     *
     * @param title the exact title
     * @return the deed if found
     */
    @GetMapping("/exact/title/{title}")
    @Operation(summary = "Get deed by exact title", description = "Retrieve a deed by exact title match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deed found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> getDeedByTitle(
            @Parameter(description = "Exact deed title", required = true)
            @PathVariable String title) {
        return deedService.getDeedByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get deeds by type and title search.
     *
     * @param type the deed type
     * @param title the title search term
     * @return list of deeds matching both criteria
     */
    @GetMapping("/filter/type/{type}/title/{title}")
    @Operation(summary = "Get deeds by type and title", description = "Retrieve deeds filtered by both type and title search term")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByTypeAndTitle(
            @Parameter(description = "Deed type", required = true, example = "SALE_DEED")
            @PathVariable DeedType type,
            @Parameter(description = "Title search term", required = true)
            @PathVariable String title) {
        return ResponseEntity.ok(deedService.getDeedsByTypeAndTitle(type, title));
    }

    /**
     * Get all parties for a deed.
     *
     * @param deedId the deed ID
     * @return list of parties in the deed
     */
    @GetMapping("/{deedId}/parties")
    @Operation(summary = "Get all parties for a deed", description = "Retrieve all parties associated with a specific deed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of parties retrieved"),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> getPartiesForDeed(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String deedId) {
        return deedService.getPartiesForDeed(deedId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a specific party from a deed.
     *
     * @param deedId the deed ID
     * @param partyId the party ID
     * @return the party if found
     */
    @GetMapping("/{deedId}/parties/{partyId}")
    @Operation(summary = "Get a specific party from a deed", description = "Retrieve a specific party associated with a deed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Party found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Party.class))),
            @ApiResponse(responseCode = "404", description = "Party or Deed not found")
    })
    public ResponseEntity<?> getPartyFromDeed(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String deedId,
            @Parameter(description = "Party ID", required = true, example = "PT00000001")
            @PathVariable String partyId) {
        return deedService.getPartyFromDeed(deedId, partyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add a party to a deed.
     *
     * @param deedId the deed ID
     * @param party the party to add
     * @return the updated deed
     */
    @PostMapping("/{deedId}/parties")
    @Operation(summary = "Add a party to a deed", description = "Associate a new party with a deed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Party added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> addPartyToDeed(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String deedId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Party object to add", required = true)
            @RequestBody Party party) {
        return deedService.addPartyToDeed(deedId, party)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a specific party within a deed.
     * 
     * Endpoint: PUT /api/deeds/{deedId}/parties/{partyId}
     * Parameters:
     *   - deedId: The Deed ID (format: DD00000001)
     *   - partyId: The Party ID to update (format: PT00000001)
     *   - Request Body: Updated Party data
     * 
     * Example Request Body:
     *   {
     *     "name": "Updated Party Name",
     *     "emailId": "updated@example.com",
     *     "phoneNumber": "9876543210",
     *     "idType": "IDENTITY_PROOF",
     *     "partyType": "PARTY_ONE"
     *   }
     *
     * @param deedId the deed ID
     * @param partyId the party ID to update
     * @param updatedPartyData the updated party information
     * @return the updated deed containing the modified party
     */
    @PutMapping("/{deedId}/parties/{partyId}")
    @Operation(summary = "Update a party in a deed", 
            description = "Update party information within a deed. Provide Deed ID and Party ID as parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Party updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed or Party not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> updatePartyInDeed(
            @Parameter(description = "Deed ID (format: DD00000001)", required = true, example = "DD00000001")
            @PathVariable String deedId,
            @Parameter(description = "Party ID to update (format: PT00000001)", required = true, example = "PT00000001")
            @PathVariable String partyId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated party data. Only provided fields will be updated.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Party.class)))
            @RequestBody Party updatedPartyData) {
        return deedService.updatePartyInDeed(deedId, partyId, updatedPartyData)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove a party from a deed.
     *
     * @param deedId the deed ID
     * @param partyId the party ID to remove
     * @return the updated deed
     */
    @DeleteMapping("/{deedId}/parties/{partyId}")
    @Operation(summary = "Remove a party from a deed", description = "Remove a party association from a deed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Party removed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> removePartyFromDeed(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String deedId,
            @Parameter(description = "Party ID to remove", required = true, example = "PT00000001")
            @PathVariable String partyId) {
        return deedService.removePartyFromDeed(deedId, partyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get deeds by status.
     *
     * @param status the deed status (DRAFT, IN_PROGRESS, COMPLETED)
     * @return list of deeds with the specified status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get deeds by status", description = "Retrieve deeds filtered by status (DRAFT, IN_PROGRESS, COMPLETED)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByStatus(
            @Parameter(description = "Deed status", required = true, example = "DRAFT")
            @PathVariable DeedStatus status) {
        return ResponseEntity.ok(deedService.getDeedsByStatus(status));
    }

    /**
     * Get deeds by type and status.
     *
     * @param type the deed type
     * @param status the deed status
     * @return list of deeds matching both criteria
     */
    @GetMapping("/filter/type/{type}/status/{status}")
    @Operation(summary = "Get deeds by type and status", description = "Retrieve deeds filtered by both type and status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByTypeAndStatus(
            @Parameter(description = "Deed type", required = true, example = "SALE_DEED")
            @PathVariable DeedType type,
            @Parameter(description = "Deed status", required = true, example = "DRAFT")
            @PathVariable DeedStatus status) {
        return ResponseEntity.ok(deedService.getDeedsByTypeAndStatus(type, status));
    }

    /**
     * Get deeds by status and title search.
     *
     * @param status the deed status
     * @param title the title search term
     * @return list of deeds matching both criteria
     */
    @GetMapping("/filter/status/{status}/title/{title}")
    @Operation(summary = "Get deeds by status and title", description = "Retrieve deeds filtered by status and title search term")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByStatusAndTitle(
            @Parameter(description = "Deed status", required = true, example = "IN_PROGRESS")
            @PathVariable DeedStatus status,
            @Parameter(description = "Title search term", required = true)
            @PathVariable String title) {
        return ResponseEntity.ok(deedService.getDeedsByStatusAndTitle(status, title));
    }

    /**
     * Update deed status.
     *
     * @param deedId the deed ID
     * @param newStatus the new status
     * @return the updated deed
     */
    @PatchMapping("/{deedId}/status/{newStatus}")
    @Operation(summary = "Update deed status", description = "Update the status of a deed (DRAFT, IN_PROGRESS, COMPLETED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class))),
            @ApiResponse(responseCode = "404", description = "Deed not found")
    })
    public ResponseEntity<?> updateDeedStatus(
            @Parameter(description = "Deed ID", required = true, example = "DD00000001")
            @PathVariable String deedId,
            @Parameter(description = "New deed status", required = true, example = "IN_PROGRESS")
            @PathVariable DeedStatus newStatus) {
        return deedService.updateDeedStatus(deedId, newStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Find deeds by party name.
     *
     * @param partyName the party name to search for
     * @return list of deeds containing a party with the specified name
     */
    @GetMapping("/search/party/name/{partyName}")
    @Operation(summary = "Find deeds by party name", description = "Search deeds containing a party with the specified name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByPartyName(
            @Parameter(description = "Party name to search for", required = true, example = "John Doe")
            @PathVariable String partyName) {
        return ResponseEntity.ok(deedService.getDeedsByPartyName(partyName));
    }

    /**
     * Find deeds by party ID.
     *
     * @param partyId the party ID to search for
     * @return list of deeds containing the specified party ID
     */
    @GetMapping("/search/party/id/{partyId}")
    @Operation(summary = "Find deeds by party ID", description = "Search deeds containing the specified party ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByPartyId(
            @Parameter(description = "Party ID to search for", required = true, example = "PT00000001")
            @PathVariable String partyId) {
        return ResponseEntity.ok(deedService.getDeedsByPartyId(partyId));
    }

    /**
     * Find deeds by party phone number.
     *
     * @param phoneNumber the phone number to search for
     * @return list of deeds containing a party with the specified phone number
     */
    @GetMapping("/search/party/phone/{phoneNumber}")
    @Operation(summary = "Find deeds by party phone number", description = "Search deeds containing a party with the specified phone number")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByPartyPhoneNumber(
            @Parameter(description = "Phone number to search for", required = true, example = "+1234567890")
            @PathVariable String phoneNumber) {
        return ResponseEntity.ok(deedService.getDeedsByPartyPhoneNumber(phoneNumber));
    }

    /**
     * Find deeds by party ID type.
     *
     * @param idType the ID type to search for
     * @return list of deeds containing a party with the specified ID type
     */
    @GetMapping("/search/party/idType/{idType}")
    @Operation(summary = "Find deeds by party ID type", description = "Search deeds containing a party with the specified ID type (e.g., ADDRESS, IDENTITY_PROOF)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deeds",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deed.class)))
    public ResponseEntity<List<Deed>> getDeedsByPartyIdType(
            @Parameter(description = "ID type to search for", required = true, example = "IDENTITY_PROOF")
            @PathVariable IdType idType) {
        return ResponseEntity.ok(deedService.getDeedsByPartyIdType(idType));
    }

}
