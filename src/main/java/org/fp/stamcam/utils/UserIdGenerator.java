package org.fp.stamcam.utils;

import org.fp.stamcam.models.User;
import org.fp.stamcam.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Utility class for generating and managing User IDs.
 * ID format: USR + 8 digits (e.g., USR00000001)
 */
@Component
public class UserIdGenerator {

    @Autowired
    private UserRepository userRepository;

    private static final String USER_PREFIX = "USR";
    private static final int ID_LENGTH = 8;
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^USR\\d{8}$");

    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique User ID with format USR + 8 digits.
     *
     * @return generated ID (e.g., USR00000001)
     */
    public synchronized String generateUserId() {
        if (currentMaxId == null) {
            List<User> allUsers = userRepository.findAll();

            long maxNumber = allUsers.stream()
                    .map(User::getId)
                    .filter(id -> id != null && USER_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(3)) // Remove "USR" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            currentMaxId = new AtomicLong(maxNumber);
        }

        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", USER_PREFIX, nextNumber);
    }
}
