package org.fp.stamcam.models;

import lombok.Getter;

/**
 * Enumeration for different types of party identification.
 */
@Getter
public enum IdType {
    USER_PHOTO("User Photo", "Photo of the user for identification"),
    ADDRESS_PROOF("Address Proof", "Address as identification"),
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

}