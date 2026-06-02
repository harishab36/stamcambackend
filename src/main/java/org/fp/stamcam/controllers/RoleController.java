package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fp.stamcam.models.Role;
import org.fp.stamcam.models.Screen;
import org.fp.stamcam.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Management", description = "APIs for managing roles and their screen permissions")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    @PostMapping
    @Operation(summary = "Create a new role", description = "Creates a new role. ID is auto-generated with format ROL + 8 digits (e.g., ROL00000001).")
    @ApiResponse(responseCode = "201", description = "Role created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)))
    public ResponseEntity<Role> createRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Role to create", required = true)
            @RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieves every role stored in the system.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieves a role by its ID (e.g., ROL00000001).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<?> getRoleById(
            @Parameter(description = "Role ID (e.g., ROL00000001)", required = true)
            @PathVariable String id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a role", description = "Updates the name and/or allowed screens of an existing role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<?> updateRole(
            @Parameter(description = "Role ID", required = true) @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated role data", required = true)
            @RequestBody Role role) {
        return roleService.updateRole(id, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role", description = "Permanently deletes a role by its ID.")
    @ApiResponse(responseCode = "204", description = "Role deleted successfully")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "Role ID", required = true) @PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // Search / find
    // -------------------------------------------------------------------------

    @GetMapping("/name/{name}")
    @Operation(summary = "Get role by exact name", description = "Returns the role whose name matches exactly.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<?> getRoleByName(
            @Parameter(description = "Exact role name", required = true) @PathVariable String name) {
        return roleService.getRoleByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search roles by name", description = "Returns roles whose name contains the given keyword (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    public ResponseEntity<List<Role>> searchRolesByName(
            @Parameter(description = "Keyword to search in role name", required = true)
            @RequestParam String name) {
        return ResponseEntity.ok(roleService.searchRolesByName(name));
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if role name exists", description = "Returns true if a role with the given name already exists.")
    @ApiResponse(responseCode = "200", description = "Existence check result")
    public ResponseEntity<Map<String, Boolean>> roleNameExists(
            @Parameter(description = "Role name to check", required = true)
            @RequestParam String name) {
        return ResponseEntity.ok(Map.of("exists", roleService.roleNameExists(name)));
    }

    @GetMapping("/with-screens")
    @Operation(summary = "Get roles that have at least one screen", description = "Returns all roles that have one or more allowed screens assigned.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> getRolesWithScreens() {
        return ResponseEntity.ok(roleService.getRolesWithScreens());
    }

    @GetMapping("/without-screens")
    @Operation(summary = "Get roles with no screens", description = "Returns all roles that have no allowed screens assigned.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> getRolesWithoutScreens() {
        return ResponseEntity.ok(roleService.getRolesWithoutScreens());
    }

    // -------------------------------------------------------------------------
    // Screen-based find
    // -------------------------------------------------------------------------

    @GetMapping("/screen/name/{screenName}")
    @Operation(summary = "Get roles by screen name", description = "Returns roles that have access to a screen with the given exact name.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> getRolesByScreenName(
            @Parameter(description = "Exact screen name", required = true)
            @PathVariable String screenName) {
        return ResponseEntity.ok(roleService.getRolesByScreenName(screenName));
    }

    @GetMapping("/screen/route")
    @Operation(summary = "Get roles by screen route", description = "Returns roles that have access to a screen with the given route path.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> getRolesByScreenRoute(
            @Parameter(description = "Screen route (e.g., /dashboard)", required = true)
            @RequestParam String route) {
        return ResponseEntity.ok(roleService.getRolesByScreenRoute(route));
    }

    @GetMapping("/screen/search")
    @Operation(summary = "Search roles by screen name", description = "Returns roles that have a screen whose name contains the keyword (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> searchRolesByScreenName(
            @Parameter(description = "Keyword to search in screen name", required = true)
            @RequestParam String name) {
        return ResponseEntity.ok(roleService.searchRolesByScreenName(name));
    }

    @GetMapping("/screen/{screenName}/count")
    @Operation(summary = "Count roles by screen name", description = "Returns the number of roles that have access to a given screen.")
    @ApiResponse(responseCode = "200", description = "Count returned")
    public ResponseEntity<Map<String, Long>> countRolesByScreenName(
            @Parameter(description = "Exact screen name", required = true)
            @PathVariable String screenName) {
        return ResponseEntity.ok(Map.of("count", roleService.countRolesByScreenName(screenName)));
    }

    // -------------------------------------------------------------------------
    // Section-based find
    // -------------------------------------------------------------------------

    @GetMapping("/section/name/{sectionName}")
    @Operation(summary = "Get roles by section name", description = "Returns roles that include a section with the given exact name in any of their screens.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> getRolesBySectionName(
            @Parameter(description = "Exact section name", required = true)
            @PathVariable String sectionName) {
        return ResponseEntity.ok(roleService.getRolesBySectionName(sectionName));
    }

    @GetMapping("/section/search")
    @Operation(summary = "Search roles by section name", description = "Returns roles that have a section whose name contains the keyword (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> searchRolesBySectionName(
            @Parameter(description = "Keyword to search in section name", required = true)
            @RequestParam String name) {
        return ResponseEntity.ok(roleService.searchRolesBySectionName(name));
    }

    @GetMapping("/screen/{screenName}/section/{sectionName}")
    @Operation(summary = "Get roles by screen and section", description = "Returns roles that have access to a specific section inside a specific screen.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved")
    public ResponseEntity<List<Role>> getRolesByScreenAndSection(
            @Parameter(description = "Screen name", required = true) @PathVariable String screenName,
            @Parameter(description = "Section name", required = true) @PathVariable String sectionName) {
        return ResponseEntity.ok(roleService.getRolesByScreenAndSection(screenName, sectionName));
    }

    // -------------------------------------------------------------------------
    // Screen management on a role
    // -------------------------------------------------------------------------

    @PostMapping("/{id}/screens")
    @Operation(summary = "Add a screen to a role", description = "Appends a new screen (with optional sections) to the role's allowed screens list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Screen added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<?> addScreenToRole(
            @Parameter(description = "Role ID", required = true) @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Screen to add", required = true)
            @RequestBody Screen screen) {
        return roleService.addScreenToRole(id, screen)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/screens/{screenName}")
    @Operation(summary = "Remove a screen from a role", description = "Removes the screen with the given name from the role's allowed screens list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Screen removed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<?> removeScreenFromRole(
            @Parameter(description = "Role ID", required = true) @PathVariable String id,
            @Parameter(description = "Screen name to remove", required = true) @PathVariable String screenName) {
        return roleService.removeScreenFromRole(id, screenName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}