package org.fp.stamcam.utils;

import org.fp.stamcam.models.PartyTitle;
import org.fp.stamcam.repositories.PartyTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Utility class for generating and managing PartyTitle IDs.
 * ID format: PRT + 4 digits (e.g., PRT0001)
 */
@Component
public class PartyTitleIdGenerator {

    @Autowired
    private PartyTitleRepository partyTitleRepository;

    private static final String PARTY_TITLE_PREFIX = "PRT";
    private static final int ID_LENGTH = 4;
    private static final Pattern PARTY_TITLE_ID_PATTERN = Pattern.compile("^PRT\\d{4}$");

    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique PartyTitle ID with format PRT + 4 digits.
     *
     * @return generated ID (e.g., PRT0001)
     */
    public synchronized String generatePartyTitleId() {
        if (currentMaxId == null) {
            // Get all existing party titles
            List<PartyTitle> allPartyTitles = partyTitleRepository.findAll();

            // Extract numeric parts from party title IDs and find the max
            long maxNumber = allPartyTitles.stream()
                    .map(PartyTitle::getId)
                    .filter(id -> id != null && PARTY_TITLE_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(3)) // Remove "PRT" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            currentMaxId = new AtomicLong(maxNumber);
        }

        // Increment and format
        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", PARTY_TITLE_PREFIX, nextNumber);
    }

    /**
     * Validate if an ID follows the PartyTitle ID format.
     *
     * @param id the ID to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidPartyTitleId(String id) {
        return id != null && PARTY_TITLE_ID_PATTERN.matcher(id).matches();
    }

    /**
     * Extract the numeric part from a PartyTitle ID.
     *
     * @param id the PartyTitle ID
     * @return numeric part as a long, or -1 if invalid
     */
    public long extractNumber(String id) {
        if (isValidPartyTitleId(id)) {
            return Long.parseLong(id.substring(3));
        }
        return -1;
    }

}

