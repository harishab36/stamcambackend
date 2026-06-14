package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * PartyTitle entity representing the title of a party within a deed.
 * This is a MongoDB document stored in the 'partyTitles' collection.
 *
 * ID format: PRT + 4 digits (e.g., PRT0001)
 */
@Document(collection = "partyTitles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyTitle {

    /**
     * Unique identifier with prefix "PRT" followed by 4 digits.
     * Format: PRT0001, PRT0002, etc.
     */
    @Id
    private String id;

    /**
     * Title of the party (e.g., Buyer, Seller, Witness, etc.).
     */
    private String title;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}

