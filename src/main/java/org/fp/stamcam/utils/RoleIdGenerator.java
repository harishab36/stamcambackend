package org.fp.stamcam.utils;

import org.fp.stamcam.models.Role;
import org.fp.stamcam.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Utility class for generating and managing Role IDs.
 * ID format: ROL + 8 digits (e.g., ROL00000001)
 */
@Component
public class RoleIdGenerator {

    @Autowired
    private RoleRepository roleRepository;

    private static final String ROLE_PREFIX = "ROL";
    private static final int ID_LENGTH = 8;
    private static final Pattern ROLE_ID_PATTERN = Pattern.compile("^ROL\\d{8}$");

    private AtomicLong currentMaxId = null;

    /**
     * Generate a new unique Role ID with format ROL + 8 digits.
     *
     * @return generated ID (e.g., ROL00000001)
     */
    public synchronized String generateRoleId() {
        if (currentMaxId == null) {
            List<Role> allRoles = roleRepository.findAll();

            long maxNumber = allRoles.stream()
                    .map(Role::getId)
                    .filter(id -> id != null && ROLE_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(3)) // Remove "ROL" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            currentMaxId = new AtomicLong(maxNumber);
        }

        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", ROLE_PREFIX, nextNumber);
    }
}
