package org.fp.stamcam.models;

/**
 * Enumeration for different types of party identification.
 */
public enum IdType {
    ADDRESS("Address", "Address as identification"),
    IDENTITY_PROOF("Identity Proof", "Valid identity document (Passport, Aadhar, etc.)");

    private final String displayName;
    private final String description;

    /**
     * Constructor for IdType.
     *
     * @param displayName the display name
     * @param description the description
     */
    IdType(String displayName, String description) {
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