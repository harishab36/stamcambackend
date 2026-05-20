package org.fp.stamcam.utils;

import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.Party;
import org.fp.stamcam.repositories.DeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for generating and managing Document IDs.
 * ID format: DOX + 8 digits (e.g., DOX00000001)
 */
@Component
public class DocumentIdGenerator {

    @Autowired
    private DeedRepository deedRepository;

    private static final String DOCUMENT_PREFIX = "DOX";
    private static final int ID_LENGTH = 8;
    private static final Pattern DOCUMENT_ID_PATTERN = Pattern.compile("^DOX\\d{8}$");

    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique Document ID with format DOX + 8 digits.
     *
     * @return generated ID (e.g., DOX00000001)
     */
    public synchronized String generateDocumentId() {
        if (currentMaxId == null) {
            // Get all existing deeds to find all documents and their max ID
            List<Deed> allDeeds = deedRepository.findAll();

            long maxNumber = allDeeds.stream()
                    .flatMap(deed -> deed.getParties().stream())
                    .flatMap(party -> party.getDocuments().stream())
                    .map(org.fp.stamcam.models.Document::getId)
                    .filter(id -> id != null && DOCUMENT_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(3)) // Remove "DOX" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            currentMaxId = new AtomicLong(maxNumber);
        }

        // Increment and format
        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", DOCUMENT_PREFIX, nextNumber);
    }
}
