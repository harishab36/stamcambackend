package org.fp.stamcam.utils;

import org.fp.stamcam.models.DeedTitle;
import org.fp.stamcam.repositories.DeedTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Utility class for generating and managing DeedTitle IDs.
 * ID format: DDTL + 4 digits (e.g., DDTL0001)
 */
@Component
public class DeedTitleIdGenerator {

    @Autowired
    private DeedTitleRepository deedTitleRepository;

    private static final String DEED_TITLE_PREFIX = "DDTL";
    private static final int ID_LENGTH = 4;
    private static final Pattern DEED_TITLE_ID_PATTERN = Pattern.compile("^DDTL\\d{4}$");

    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique DeedTitle ID with format DDTL + 4 digits.
     *
     * @return generated ID (e.g., DDTL0001)
     */
    public synchronized String generateDeedTitleId() {
        if (currentMaxId == null) {
            // Get all existing deed titles
            List<DeedTitle> allDeedTitles = deedTitleRepository.findAll();

            // Extract numeric parts from deed title IDs and find the max
            long maxNumber = allDeedTitles.stream()
                    .map(DeedTitle::getId)
                    .filter(id -> id != null && DEED_TITLE_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(4)) // Remove "DDTL" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            currentMaxId = new AtomicLong(maxNumber);
        }

        // Increment and format
        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", DEED_TITLE_PREFIX, nextNumber);
    }

    /**
     * Validate if an ID follows the DeedTitle ID format.
     *
     * @param id the ID to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidDeedTitleId(String id) {
        return id != null && DEED_TITLE_ID_PATTERN.matcher(id).matches();
    }

    /**
     * Extract the numeric part from a DeedTitle ID.
     *
     * @param id the DeedTitle ID
     * @return numeric part as a long, or -1 if invalid
     */
    public long extractNumber(String id) {
        if (isValidDeedTitleId(id)) {
            return Long.parseLong(id.substring(4));
        }
        return -1;
    }

}

