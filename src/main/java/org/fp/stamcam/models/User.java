package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing an application user.
 * This is a MongoDB document stored in the 'users' collection.
 *
 * ID format: USR + 8 digits (e.g., USR00000001)
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    /**
     * IDs of the roles assigned to this user.
     * One user can hold many roles (one-to-many reference to the 'roles' collection).
     */
    @Builder.Default
    private List<String> roleIds = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
