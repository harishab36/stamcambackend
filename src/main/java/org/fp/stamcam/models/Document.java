package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Represents a document associated with a party, such as an ID proof or address proof.
 * The document's image is stored as a byte array.
 *
 * ID format: DOX + 8 digits (e.g., DOX00000001)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    /**
     * Unique identifier with prefix "DOX" followed by 8 digits.
     */
    @Id
    private String id;

    /**
     * The type of document (e.g., ADDRESS, IDENTITY_PROOF).
     */
    private IdType idType;

    /**
     * The image data of the document, stored as a byte array.
     */
    private String base64Image;
}
