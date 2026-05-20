package org.fp.stamcam.models;

/**
 * Enumeration for different types of deeds.
 */
public enum DeedType {
    SALE_DEED("Sale Deed", "Property sale transfer"),
    GIFT_DEED("Gift Deed", "Property given as a gift"),
    MORTGAGE_DEED("Mortgage Deed", "Property mortgaged"),
    LEASE_DEED("Lease Deed", "Property leased"),
    EXCHANGE_DEED("Exchange Deed", "Property exchanged"),
    PARTITION_DEED("Partition Deed", "Property partition"),
    DONATION_DEED("Donation Deed", "Property donation"),
    RELEASE_DEED("Release Deed", "Release of rights"),
    TRANSFER_DEED("Transfer Deed", "General property transfer"),
    AFFIDAVIT_DEED("Affidavit Deed", "Affidavit-based transfer");

    private final String displayName;
    private final String description;

    /**
     * Constructor for DeedType.
     *
     * @param displayName the display name
     * @param description the description
     */
    DeedType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the display name.
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }
}

