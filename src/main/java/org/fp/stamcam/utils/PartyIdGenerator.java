package org.fp.stamcam.utils;

import org.fp.stamcam.models.Party;
import org.fp.stamcam.repositories.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Utility class for generating and managing Party IDs.
 * ID format: PT + 8 digits (e.g., PT00000001)
 */
@Component
public class PartyIdGenerator {

    @Autowired
    private PartyRepository partyRepository;

    private static final String PARTY_PREFIX = "PT";
    private static final int ID_LENGTH = 8;
    private static final Pattern PARTY_ID_PATTERN = Pattern.compile("^PT\\d{8}$");

    // Keep track of the last used ID in memory to ensure uniqueness
    // across multiple requests before they are saved to the database
    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique Party ID with format PT + 8 digits.
     * Ensures uniqueness even when generating multiple IDs in rapid succession.
     *
     * @return generated ID (e.g., PT00000001)
     */
    public synchronized String generatePartyId() {
        if (currentMaxId == null) {
            // Get all existing parties from database to find the absolute max
            List<Party> allParties = partyRepository.findAll();

            // Extract numeric parts from party IDs and find the max
            long maxNumber = allParties.stream()
                    .map(Party::getId)
                    .filter(id -> id != null && PARTY_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(2)) // Remove "PT" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);
                    
            currentMaxId = new AtomicLong(maxNumber);
        }

        // Increment and format
        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", PARTY_PREFIX, nextNumber);
    }

    /**
     * Validate if an ID follows the Party ID format.
     *
     * @param id the ID to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidPartyId(String id) {
        return id != null && PARTY_ID_PATTERN.matcher(id).matches();
    }

    /**
     * Extract the numeric part from a Party ID.
     *
     * @param id the Party ID
     * @return numeric part as a long, or -1 if invalid
     */
    public long extractNumber(String id) {
        if (isValidPartyId(id)) {
            return Long.parseLong(id.substring(2));
        }
        return -1;
    }

}