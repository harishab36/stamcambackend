package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Party entity representing a party involved in a legal deed.
 * This is a MongoDB document stored in the 'parties' collection.
 *
 * ID format: PT + 8 digits (e.g., PT00000001)
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "parties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {

    /**
     * Unique identifier with prefix "PT" followed by 8 digits.
     * Format: PT00000001, PT00000002, etc.
     */
    @Id
    private String id;

    /**
     * Name of the party.
     */
    private String name;

    /**
     * Email ID of the party.
     */
    private String emailId;

    /**
     * Phone number of the party.
     */
    private String phoneNumber;

    /**
     * Type of party in the deed (Party One, Party Second, or Third Party).
     */
    private PartyType partyType;

    /**
     * List of documents associated with the party.
     */
    private List<Document> documents;

    /**
     * Timestamp when the party record was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the party record was last updated.
     */
    private LocalDateTime updatedAt;

}