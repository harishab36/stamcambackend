package org.fp.stamcam.models;

/**
 * Enumeration for different types of parties in a deed.
 */
public enum PartyType {
    FIRST_PARTY("First Party", "First party to the deed"),
    SECOND_PARTY("Second Second", "Second party to the deed"),
    THIRD_PARTY("Third Party", "Third party involved");

    private final String displayName;
    private final String description;

    /**
     * Constructor for PartyType.
     *
     * @param displayName the display name
     * @param description the description
     */
    PartyType(String displayName, String description) {
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