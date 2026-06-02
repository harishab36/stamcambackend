package org.fp.stamcam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fp.stamcam.models.Role;
import org.fp.stamcam.models.User;
import org.fp.stamcam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for User operations.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user. The ID will be auto-generated with the format USR + 8 digits (e.g., USR00000001).")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    public ResponseEntity<User> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object to be created", required = true)
            @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    /**
     * Get all users.
     *
     * @return list of all users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(
            @Parameter(description = "User ID (e.g., USR00000001)", required = true)
            @PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a user by username.
     *
     * @param username the username
     * @return the user if found
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a specific user by their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserByUsername(
            @Parameter(description = "Username to search for", required = true)
            @PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update an existing user.
     *
     * @param id the user ID
     * @param user the updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates an existing user. Only the provided fields will be updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> updateUser(
            @Parameter(description = "User ID (e.g., USR00000001)", required = true)
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user data", required = true)
            @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a user.
     *
     * @param id the user ID
     * @return success response
     */
    @PostMapping("/login")
    @Operation(summary = "Find user by username and password", description = "Authenticates a user by matching username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<?> getUserByUsernameAndPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login credentials", required = true)
            @RequestBody User user) {
        return userService.getUserByUsernameAndPassword(user.getUsername(), user.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user by their ID.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID (e.g., USR00000001)", required = true)
            @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // Role assignment
    // -------------------------------------------------------------------------

    @PostMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Assign a role to a user", description = "Adds a role to the user's role list. No-op if already assigned. Returns 404 if the user or role does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role assigned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    public ResponseEntity<?> assignRoleToUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id,
            @Parameter(description = "Role ID (e.g., ROL00000001)", required = true) @PathVariable String roleId) {
        return userService.assignRoleToUser(id, roleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Remove a role from a user", description = "Removes a role from the user's role list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role removed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> removeRoleFromUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id,
            @Parameter(description = "Role ID to remove", required = true) @PathVariable String roleId) {
        return userService.removeRoleFromUser(id, roleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/roles")
    @Operation(summary = "Replace all roles of a user", description = "Replaces the user's entire role list with the provided role IDs. Invalid IDs are silently ignored.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> setUserRoles(
            @Parameter(description = "User ID", required = true) @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of role IDs to assign", required = true)
            @RequestBody List<String> roleIds) {
        return userService.setUserRoles(id, roleIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/roles")
    @Operation(summary = "Get roles of a user", description = "Returns the full Role objects assigned to the user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<Role>> getUserRoles(
            @Parameter(description = "User ID", required = true) @PathVariable String id) {
        return userService.getUserRoles(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------------------------------------------------------------
    // Role-based user queries
    // -------------------------------------------------------------------------

    @GetMapping("/role/{roleId}")
    @Operation(summary = "Get users by role ID", description = "Returns all users that have the specified role assigned.")
    @ApiResponse(responseCode = "200", description = "Users returned")
    public ResponseEntity<List<User>> getUsersByRoleId(
            @Parameter(description = "Role ID", required = true) @PathVariable String roleId) {
        return ResponseEntity.ok(userService.getUsersByRoleId(roleId));
    }

    @GetMapping("/role/{roleId}/count")
    @Operation(summary = "Count users by role ID", description = "Returns the number of users assigned to the specified role.")
    @ApiResponse(responseCode = "200", description = "Count returned")
    public ResponseEntity<Map<String, Long>> countUsersByRoleId(
            @Parameter(description = "Role ID", required = true) @PathVariable String roleId) {
        return ResponseEntity.ok(Map.of("count", userService.countUsersByRoleId(roleId)));
    }

    @GetMapping("/no-roles")
    @Operation(summary = "Get users with no roles", description = "Returns all users that have no roles assigned.")
    @ApiResponse(responseCode = "200", description = "Users returned")
    public ResponseEntity<List<User>> getUsersWithNoRoles() {
        return ResponseEntity.ok(userService.getUsersWithNoRoles());
    }

    @PostMapping("/by-roles/all")
    @Operation(summary = "Get users that have all of the given roles", description = "Returns users who have every role in the provided list.")
    @ApiResponse(responseCode = "200", description = "Users returned")
    public ResponseEntity<List<User>> getUsersByAllRoles(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of role IDs — all must be present", required = true)
            @RequestBody List<String> roleIds) {
        return ResponseEntity.ok(userService.getUsersByAllRoleIds(roleIds));
    }

    @PostMapping("/by-roles/any")
    @Operation(summary = "Get users that have any of the given roles", description = "Returns users who have at least one role from the provided list.")
    @ApiResponse(responseCode = "200", description = "Users returned")
    public ResponseEntity<List<User>> getUsersByAnyRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of role IDs — at least one must be present", required = true)
            @RequestBody List<String> roleIds) {
        return ResponseEntity.ok(userService.getUsersByAnyRoleId(roleIds));
    }
}
