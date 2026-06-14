package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fp.stamcam.models.PartyTitle;
import org.fp.stamcam.services.PartyTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for party title operations.
 * Provides endpoints for CRUD operations on party titles.
 */
@RestController
@RequestMapping("/api/party-titles")
@Tag(name = "Party Title Management", description = "APIs for managing party titles within deeds")
public class PartyTitleController {

    @Autowired
    private PartyTitleService partyTitleService;

    /**
     * Get all party titles.
     *
     * @return list of all party titles
     */
    @GetMapping
    @Operation(summary = "Get all party titles", description = "Retrieve a list of all party titles in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved party titles",
            content = @Content(schema = @Schema(implementation = PartyTitle.class)))
    public ResponseEntity<List<PartyTitle>> getAllPartyTitles() {
        List<PartyTitle> partyTitles = partyTitleService.getAllPartyTitles();
        return ResponseEntity.ok(partyTitles);
    }

    /**
     * Get a party title by ID.
     *
     * @param id the party title ID
     * @return the party title if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get party title by ID", description = "Retrieve a specific party title by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved party title",
                    content = @Content(schema = @Schema(implementation = PartyTitle.class))),
            @ApiResponse(responseCode = "404", description = "Party title not found")
    })
    public ResponseEntity<PartyTitle> getPartyTitleById(@PathVariable String id) {
        Optional<PartyTitle> partyTitle = partyTitleService.getPartyTitleById(id);
        return partyTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Search party titles by title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of matching party titles
     */
    @GetMapping("/search")
    @Operation(summary = "Search party titles by title", description = "Search for party titles using case-insensitive title matching")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved matching party titles",
            content = @Content(schema = @Schema(implementation = PartyTitle.class)))
    public ResponseEntity<List<PartyTitle>> searchPartyTitles(@RequestParam String title) {
        List<PartyTitle> partyTitles = partyTitleService.getPartyTitlesByTitle(title);
        return ResponseEntity.ok(partyTitles);
    }

    /**
     * Create a new party title.
     *
     * @param title the title of the party
     * @return the created PartyTitle
     */
    @PostMapping
    @Operation(summary = "Create a new party title", description = "Create a new party title with auto-generated ID")
    @ApiResponse(responseCode = "201", description = "Successfully created party title",
            content = @Content(schema = @Schema(implementation = PartyTitle.class)))
    public ResponseEntity<PartyTitle> createPartyTitle(@RequestParam String title) {
        PartyTitle createdPartyTitle = partyTitleService.createPartyTitle(title);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartyTitle);
    }

    /**
     * Update a party title.
     *
     * @param id the party title ID
     * @param title the new title
     * @return the updated PartyTitle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a party title", description = "Update the title of an existing party title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated party title",
                    content = @Content(schema = @Schema(implementation = PartyTitle.class))),
            @ApiResponse(responseCode = "404", description = "Party title not found")
    })
    public ResponseEntity<PartyTitle> updatePartyTitle(@PathVariable String id, @RequestParam String title) {
        Optional<PartyTitle> updatedPartyTitle = partyTitleService.updatePartyTitle(id, title);
        return updatedPartyTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a party title.
     *
     * @param id the party title ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a party title", description = "Delete a party title by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted party title"),
            @ApiResponse(responseCode = "404", description = "Party title not found")
    })
    public ResponseEntity<Void> deletePartyTitle(@PathVariable String id) {
        boolean deleted = partyTitleService.deletePartyTitle(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}

