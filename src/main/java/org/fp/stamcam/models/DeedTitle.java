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
import java.util.ArrayList;
import java.util.List;

/**
 * DeedTitle entity representing a configurable deed with its associated party titles.
 * This is a MongoDB document stored in the 'deedTitles' collection.
 *
 * ID format: DDTL + 4 digits (e.g., DDTL0001)
 */
@Document(collection = "deedTitles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeedTitle {

    /**
     * Unique identifier with prefix "DDTL" followed by 4 digits.
     * Format: DDTL0001, DDTL0002, etc.
     */
    @Id
    private String id;

    /**
     * Title of the deed (e.g., Sale Deed, Gift Deed, Mortgage Deed, etc.).
     */
    private String title;

    /**
     * List of party titles associated with this deed template.
     * Party titles define the roles that parties can have in this deed.
     */
    @Builder.Default
    private List<PartyTitle> partyTitles = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}

