package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fp.stamcam.models.Zone;
import org.fp.stamcam.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@Tag(name = "Zone Management", description = "APIs for managing zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping
    @Operation(summary = "Get all zones", description = "Retrieve a list of all zones")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved zones",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Zone.class)))
    public ResponseEntity<List<Zone>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get zone by ID", description = "Retrieve a specific zone by its ID (format: ZON + 8 digits)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zone found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Zone.class))),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    public ResponseEntity<Zone> getZoneById(
            @Parameter(description = "Zone ID (format: ZON00000001)", required = true, example = "ZON00000001")
            @PathVariable String id) {
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new zone", description = "Create a new zone. ID will be auto-generated with format ZON + 8 digits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zone created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Zone.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request — title is required")
    })
    public ResponseEntity<Zone> createZone(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Zone object to create", required = true)
            @RequestBody Zone zone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.createZone(zone));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a zone", description = "Update an existing zone's title or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zone updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Zone.class))),
            @ApiResponse(responseCode = "404", description = "Zone not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Zone> updateZone(
            @Parameter(description = "Zone ID", required = true, example = "ZON00000001")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated zone data", required = true)
            @RequestBody Zone zone) {
        return ResponseEntity.ok(zoneService.updateZone(id, zone));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a zone", description = "Delete a zone by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Zone deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    public ResponseEntity<Void> deleteZone(
            @Parameter(description = "Zone ID", required = true, example = "ZON00000001")
            @PathVariable String id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }
}