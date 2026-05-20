package org.fp.stamcam.utils;

import org.fp.stamcam.models.Deed;
import org.fp.stamcam.repositories.DeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Utility class for generating and managing Deed IDs.
 * ID format: DD + 8 digits (e.g., DD00000001)
 */
@Component
public class DeedIdGenerator {

    @Autowired
    private DeedRepository deedRepository;

    private static final String DEED_PREFIX = "DD";
    private static final int ID_LENGTH = 8;
    private static final Pattern DEED_ID_PATTERN = Pattern.compile("^DD\\d{8}$");

    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique Deed ID with format DD + 8 digits.
     *
     * @return generated ID (e.g., DD00000001)
     */
    public synchronized String generateDeedId() {
        if (currentMaxId == null) {
            // Get all existing deeds
            List<Deed> allDeeds = deedRepository.findAll();

            // Extract numeric parts from deed IDs and find the max
            long maxNumber = allDeeds.stream()
                    .map(Deed::getId)
                    .filter(id -> id != null && DEED_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(2)) // Remove "DD" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);
                    
            currentMaxId = new AtomicLong(maxNumber);
        }

        // Increment and format
        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", DEED_PREFIX, nextNumber);
    }

    /**
     * Validate if an ID follows the Deed ID format.
     *
     * @param id the ID to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidDeedId(String id) {
        return id != null && DEED_ID_PATTERN.matcher(id).matches();
    }

    /**
     * Extract the numeric part from a Deed ID.
     *
     * @param id the Deed ID
     * @return numeric part as a long, or -1 if invalid
     */
    public long extractNumber(String id) {
        if (isValidDeedId(id)) {
            return Long.parseLong(id.substring(2));
        }
        return -1;
    }

}
