package org.fp.stamcam.models;

/**
 * Enumeration for different statuses of a deed document.
 */
public enum DeedStatus {
    DRAFT("Draft", "Deed is in draft status, not yet finalized"),
    IN_PROGRESS("In Progress", "Deed is currently being processed"),
    COMPLETED("Completed", "Deed has been completed and finalized");

    private final String displayName;
    private final String description;

    /**
     * Constructor for DeedStatus.
     *
     * @param displayName the display name
     * @param description the description
     */
    DeedStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

