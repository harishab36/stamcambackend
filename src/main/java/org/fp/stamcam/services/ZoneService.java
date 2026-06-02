package org.fp.stamcam.services;

import org.fp.stamcam.exceptions.ZoneNotFoundException;
import org.fp.stamcam.models.Zone;
import org.fp.stamcam.repositories.ZoneRepository;
import org.fp.stamcam.utils.ZoneIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneIdGenerator zoneIdGenerator;

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Zone getZoneById(String id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ZoneNotFoundException(id));
    }

    public Zone createZone(Zone zone) {
        if (zone.getTitle() == null || zone.getTitle().isBlank()) {
            throw new IllegalArgumentException("Zone title must not be blank");
        }
        zone.setId(zoneIdGenerator.generateZoneId());
        return zoneRepository.save(zone);
    }

    public Zone updateZone(String id, Zone zone) {
        Zone existing = zoneRepository.findById(id)
                .orElseThrow(() -> new ZoneNotFoundException(id));

        if (zone.getTitle() != null) {
            if (zone.getTitle().isBlank()) {
                throw new IllegalArgumentException("Zone title must not be blank");
            }
            existing.setTitle(zone.getTitle());
        }
        if (zone.getDescription() != null) {
            existing.setDescription(zone.getDescription());
        }
        return zoneRepository.save(existing);
    }

    public void deleteZone(String id) {
        if (!zoneRepository.existsById(id)) {
            throw new ZoneNotFoundException(id);
        }
        zoneRepository.deleteById(id);
    }
}