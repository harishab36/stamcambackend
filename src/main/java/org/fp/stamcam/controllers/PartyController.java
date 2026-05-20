package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fp.stamcam.models.Party;
import org.fp.stamcam.models.PartyType;
import org.fp.stamcam.models.IdType;
import org.fp.stamcam.services.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Party management.
 * Provides endpoints for managing parties in legal deeds.
 */
@RestController
@RequestMapping("/api/parties")
@Tag(name = "Party Management", description = "APIs for managing parties involved in legal deeds")
public class PartyController {

    @Autowired
    private PartyService partyService;

    /**
     * Create a new party.
     */
    @PostMapping
    @Operation(summary = "Create a new party", description = "Create a new party with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Party created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Party> createParty(@RequestBody Party party) {
        Party createdParty = partyService.createParty(party);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParty);
    }

    /**
     * Get a party by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get party by ID", description = "Retrieve a specific party by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Party found"),
            @ApiResponse(responseCode = "404", description = "Party not found")
    })
    public ResponseEntity<Party> getPartyById(
            @Parameter(description = "Party ID (e.g., PT00000001)")
            @PathVariable String id) {
        Optional<Party> party = partyService.getPartyById(id);
        return party.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all parties.
     */
    @GetMapping
    @Operation(summary = "Get all parties", description = "Retrieve all parties in the system")
    @ApiResponse(responseCode = "200", description = "List of parties retrieved")
    public ResponseEntity<List<Party>> getAllParties() {
        List<Party> parties = partyService.getAllParties();
        return ResponseEntity.ok(parties);
    }

    /**
     * Update a party.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a party", description = "Update party details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Party updated successfully"),
            @ApiResponse(responseCode = "404", description = "Party not found")
    })
    public ResponseEntity<Party> updateParty(
            @Parameter(description = "Party ID")
            @PathVariable String id,
            @RequestBody Party partyDetails) {
        Optional<Party> updatedParty = partyService.updateParty(id, partyDetails);
        return updatedParty.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a party.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a party", description = "Delete a party from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Party deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Party not found")
    })
    public ResponseEntity<Void> deleteParty(
            @Parameter(description = "Party ID")
            @PathVariable String id) {
        if (partyService.deleteParty(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get parties by party type.
     */
    @GetMapping("/type/{partyType}")
    @Operation(summary = "Get parties by type", description = "Retrieve all parties of a specific type")
    @ApiResponse(responseCode = "200", description = "List of parties retrieved")
    public ResponseEntity<List<Party>> getPartiesByType(
            @Parameter(description = "Party type (FIRST_PARTY, SECOND_PARTY, THIRD_PARTY)")
            @PathVariable PartyType partyType) {
        List<Party> parties = partyService.getPartiesByType(partyType);
        return ResponseEntity.ok(parties);
    }

    /**
     * Get party by email.
     */
    @GetMapping("/email/{emailId}")
    @Operation(summary = "Get party by email", description = "Retrieve a party by email ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Party found"),
            @ApiResponse(responseCode = "404", description = "Party not found")
    })
    public ResponseEntity<Party> getPartyByEmailId(
            @Parameter(description = "Email ID")
            @PathVariable String emailId) {
        Optional<Party> party = partyService.getPartyByEmailId(emailId);
        return party.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Search parties by name.
     */
    @GetMapping("/search")
    @Operation(summary = "Search parties by name", description = "Search for parties containing the specified name")
    @ApiResponse(responseCode = "200", description = "Search results")
    public ResponseEntity<List<Party>> searchPartiesByName(
            @Parameter(description = "Name search term")
            @RequestParam String name) {
        List<Party> parties = partyService.searchPartiesByName(name);
        return ResponseEntity.ok(parties);
    }

}