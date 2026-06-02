package org.fp.stamcam.utils;

import org.fp.stamcam.models.Zone;
import org.fp.stamcam.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@Component
public class ZoneIdGenerator {

    @Autowired
    private ZoneRepository zoneRepository;

    private static final String ZONE_PREFIX = "ZON";
    private static final int ID_LENGTH = 8;
    private static final Pattern ZONE_ID_PATTERN = Pattern.compile("^ZON\\d{8}$");

    private AtomicLong currentMaxId = null;

    public synchronized String generateZoneId() {
        if (currentMaxId == null) {
            List<Zone> allZones = zoneRepository.findAll();

            long maxNumber = allZones.stream()
                    .map(Zone::getId)
                    .filter(id -> id != null && ZONE_ID_PATTERN.matcher(id).matches())
                    .map(id -> id.substring(3)) // Remove "ZON" prefix
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            currentMaxId = new AtomicLong(maxNumber);
        }

        long nextNumber = currentMaxId.incrementAndGet();
        return String.format("%s%0" + ID_LENGTH + "d", ZONE_PREFIX, nextNumber);
    }

    public boolean isValidZoneId(String id) {
        return id != null && ZONE_ID_PATTERN.matcher(id).matches();
    }
}