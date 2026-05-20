package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import org.fp.stamcam.models.Party;

/**
 * Deed entity representing a legal deed document in the system.
 * This is a MongoDB document stored in the 'deeds' collection.
 * 
 * ID format: DD + 8 digits (e.g., DD00000001)
 */
@Document(collection = "deeds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deed {

    /**
     * Unique identifier with prefix "DD" followed by 8 digits.
     * Format: DD00000001, DD00000002, etc.
     */
    @Id
    private String id;

    /**
     * Title of the deed.
     */
    private String title;

    /**
     * Content of the deed as a large text blob.
     * Can contain the full text of the deed document.
     */
    private String matter;

    /**
     * Type of the deed (Sale Deed, Gift Deed, etc.).
     */
    private DeedType type;

    /**
     * Parties involved in the deed.
     */
    private List<Party> parties;

    /**
     * Status of the deed (Draft, In Progress, Completed).
     */
    private DeedStatus status;

    /**
     * Timestamp when the deed was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the deed was last updated.
     */
    private LocalDateTime updatedAt;

}

