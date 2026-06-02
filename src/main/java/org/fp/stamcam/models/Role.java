package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Role entity representing a user role with specific permissions.
 * This is a MongoDB document stored in the 'roles' collection.
 *
 * ID format: ROL + 8 digits (e.g., ROL00000001)
 */
@Document(collection = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /**
     * Unique identifier with prefix "ROL" followed by 8 digits.
     */
    @Id
    private String id;

    /**
     * Name of the role (e.g., "Admin", "Editor").
     */
    private String name;

    /**
     * List of screens that this role is allowed to access.
     */
    private List<Screen> allowedScreens;
}
