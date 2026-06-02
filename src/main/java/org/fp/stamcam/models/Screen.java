package org.fp.stamcam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a screen or page in the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {

    /**
     * Name or identifier of the screen.
     */
    private String name;

    /**
     * Route or path associated with the screen.
     */
    private String route;

    /**
     * List of specific sections within the screen that are accessible.
     */
    private List<Section> allowedSections;
}
