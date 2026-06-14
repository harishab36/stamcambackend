package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fp.stamcam.models.DeedTitle;
import org.fp.stamcam.models.PartyTitle;
import org.fp.stamcam.services.DeedTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for deed title operations.
 * Provides endpoints for CRUD operations on deed titles and management of associated party titles.
 */
@RestController
@RequestMapping("/api/deed-titles")
@Tag(name = "Deed Title Management", description = "APIs for managing deed titles and their associated party titles")
public class DeedTitleController {

    @Autowired
    private DeedTitleService deedTitleService;

    /**
     * Get all deed titles.
     *
     * @return list of all deed titles
     */
    @GetMapping
    @Operation(summary = "Get all deed titles", description = "Retrieve a list of all deed titles in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved deed titles",
            content = @Content(schema = @Schema(implementation = DeedTitle.class)))
    public ResponseEntity<List<DeedTitle>> getAllDeedTitles() {
        List<DeedTitle> deedTitles = deedTitleService.getAllDeedTitles();
        return ResponseEntity.ok(deedTitles);
    }

    /**
     * Get a deed title by ID.
     *
     * @param id the deed title ID
     * @return the deed title if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get deed title by ID", description = "Retrieve a specific deed title by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved deed title",
                    content = @Content(schema = @Schema(implementation = DeedTitle.class))),
            @ApiResponse(responseCode = "404", description = "Deed title not found")
    })
    public ResponseEntity<DeedTitle> getDeedTitleById(@PathVariable String id) {
        Optional<DeedTitle> deedTitle = deedTitleService.getDeedTitleById(id);
        return deedTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Search deed titles by title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of matching deed titles
     */
    @GetMapping("/search")
    @Operation(summary = "Search deed titles by title", description = "Search for deed titles using case-insensitive title matching")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved matching deed titles",
            content = @Content(schema = @Schema(implementation = DeedTitle.class)))
    public ResponseEntity<List<DeedTitle>> searchDeedTitles(@RequestParam String title) {
        List<DeedTitle> deedTitles = deedTitleService.getDeedTitlesByTitle(title);
        return ResponseEntity.ok(deedTitles);
    }

    /**
     * Create a new deed title.
     *
     * @param title the title of the deed
     * @return the created DeedTitle
     */
    @PostMapping
    @Operation(summary = "Create a new deed title", description = "Create a new deed title with auto-generated ID")
    @ApiResponse(responseCode = "201", description = "Successfully created deed title",
            content = @Content(schema = @Schema(implementation = DeedTitle.class)))
    public ResponseEntity<DeedTitle> createDeedTitle(@RequestParam String title) {
        DeedTitle createdDeedTitle = deedTitleService.createDeedTitle(title);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeedTitle);
    }

    /**
     * Update a deed title.
     *
     * @param id the deed title ID
     * @param title the new title
     * @return the updated DeedTitle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a deed title", description = "Update the title of an existing deed title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated deed title",
                    content = @Content(schema = @Schema(implementation = DeedTitle.class))),
            @ApiResponse(responseCode = "404", description = "Deed title not found")
    })
    public ResponseEntity<DeedTitle> updateDeedTitle(@PathVariable String id, @RequestParam String title) {
        Optional<DeedTitle> updatedDeedTitle = deedTitleService.updateDeedTitle(id, title);
        return updatedDeedTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a deed title.
     *
     * @param id the deed title ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a deed title", description = "Delete a deed title by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted deed title"),
            @ApiResponse(responseCode = "404", description = "Deed title not found")
    })
    public ResponseEntity<Void> deleteDeedTitle(@PathVariable String id) {
        boolean deleted = deedTitleService.deleteDeedTitle(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Add a party title to a deed title.
     *
     * @param deedTitleId the deed title ID
     * @param partyTitleId the party title ID to add
     * @return the updated DeedTitle
     */
    @PostMapping("/{deedTitleId}/party-titles/{partyTitleId}")
    @Operation(summary = "Add party title to deed title", description = "Add an existing party title to a deed title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully added party title to deed",
                    content = @Content(schema = @Schema(implementation = DeedTitle.class))),
            @ApiResponse(responseCode = "404", description = "Deed title or party title not found")
    })
    public ResponseEntity<DeedTitle> addPartyTitle(@PathVariable String deedTitleId, @PathVariable String partyTitleId) {
        Optional<DeedTitle> updatedDeedTitle = deedTitleService.addPartyTitle(deedTitleId, partyTitleId);
        return updatedDeedTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Remove a party title from a deed title.
     *
     * @param deedTitleId the deed title ID
     * @param partyTitleId the party title ID to remove
     * @return the updated DeedTitle
     */
    @DeleteMapping("/{deedTitleId}/party-titles/{partyTitleId}")
    @Operation(summary = "Remove party title from deed title", description = "Remove a party title from a deed title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully removed party title from deed",
                    content = @Content(schema = @Schema(implementation = DeedTitle.class))),
            @ApiResponse(responseCode = "404", description = "Deed title not found")
    })
    public ResponseEntity<DeedTitle> removePartyTitle(@PathVariable String deedTitleId, @PathVariable String partyTitleId) {
        Optional<DeedTitle> updatedDeedTitle = deedTitleService.removePartyTitle(deedTitleId, partyTitleId);
        return updatedDeedTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update a party title within a deed title.
     *
     * @param deedTitleId the deed title ID
     * @param partyTitleId the party title ID
     * @param newTitle the new title for the party
     * @return the updated DeedTitle
     */
    @PutMapping("/{deedTitleId}/party-titles/{partyTitleId}")
    @Operation(summary = "Update party title in deed title", description = "Update a party title within a deed title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated party title in deed",
                    content = @Content(schema = @Schema(implementation = DeedTitle.class))),
            @ApiResponse(responseCode = "404", description = "Deed title or party title not found")
    })
    public ResponseEntity<DeedTitle> updatePartyTitleInDeed(@PathVariable String deedTitleId, @PathVariable String partyTitleId, @RequestParam String newTitle) {
        Optional<DeedTitle> updatedDeedTitle = deedTitleService.updatePartyTitleInDeed(deedTitleId, partyTitleId, newTitle);
        return updatedDeedTitle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all party titles for a deed title.
     *
     * @param deedTitleId the deed title ID
     * @return list of party titles
     */
    @GetMapping("/{deedTitleId}/party-titles")
    @Operation(summary = "Get party titles for deed title", description = "Retrieve all party titles associated with a deed title")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved party titles",
            content = @Content(schema = @Schema(implementation = PartyTitle.class)))
    public ResponseEntity<List<PartyTitle>> getPartyTitlesForDeed(@PathVariable String deedTitleId) {
        List<PartyTitle> partyTitles = deedTitleService.getPartyTitlesForDeed(deedTitleId);
        return ResponseEntity.ok(partyTitles);
    }

}

