package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a specific section within a screen that a role can access.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

    /**
     * Name or identifier of the section.
     */
    private String name;

    /**
     * Description of the section's purpose.
     */
    private String description;
}
