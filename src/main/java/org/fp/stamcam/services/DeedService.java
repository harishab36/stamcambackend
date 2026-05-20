package org.fp.stamcam.services;

import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.DeedType;
import org.fp.stamcam.models.DeedStatus;
import org.fp.stamcam.models.IdType;
import org.fp.stamcam.models.Party;
import org.fp.stamcam.models.Document;
import org.fp.stamcam.repositories.DeedRepository;
import org.fp.stamcam.utils.DeedIdGenerator;
import org.fp.stamcam.utils.PartyIdGenerator;
import org.fp.stamcam.utils.DocumentIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Deed operations.
 * Contains business logic for managing deed entities.
 */
@Service
public class DeedService {

    @Autowired
    private DeedRepository deedRepository;

    @Autowired
    private DeedIdGenerator deedIdGenerator;

    @Autowired
    private PartyIdGenerator partyIdGenerator;

    @Autowired
    private DocumentIdGenerator documentIdGenerator;

    /**
     * Get all deeds.
     *
     * @return list of all deeds
     */
    public List<Deed> getAllDeeds() {
        return deedRepository.findAll();
    }

    /**
     * Get a deed by ID.
     *
     * @param id the deed ID
     * @return optional containing the deed if found
     */
    public Optional<Deed> getDeedById(String id) {
        return deedRepository.findById(id);
    }

    /**
     * Create a new deed with auto-generated ID.
     * Also auto-generates IDs for all parties included in the deed.
     *
     * @param deed the deed to create
     * @return the created deed with generated ID and default DRAFT status
     */
    public Deed createDeed(Deed deed) {
        // Auto-generate the ID with format DD + 8 digits
        String generatedId = deedIdGenerator.generateDeedId();
        deed.setId(generatedId);
        // Set default status to DRAFT if not specified
        if (deed.getStatus() == null) {
            deed.setStatus(DeedStatus.DRAFT);
        }
        deed.setCreatedAt(LocalDateTime.now());
        deed.setUpdatedAt(LocalDateTime.now());

        // Generate IDs and set timestamps for all parties
        if (deed.getParties() != null) {
            for (Party party : deed.getParties()) {
                if (party.getId() == null || party.getId().trim().isEmpty()) {
                    party.setId(partyIdGenerator.generatePartyId());
                }
                LocalDateTime now = LocalDateTime.now();
                if (party.getCreatedAt() == null) {
                    party.setCreatedAt(now);
                }
                if (party.getUpdatedAt() == null) {
                    party.setUpdatedAt(now);
                }

                // Generate IDs for any attached documents
                if (party.getDocuments() != null) {
                    for (Document doc : party.getDocuments()) {
                        if (doc.getId() == null || doc.getId().trim().isEmpty()) {
                            doc.setId(documentIdGenerator.generateDocumentId());
                        }
                    }
                }
            }
        }

        return deedRepository.save(deed);
    }

    /**
     * Update an existing deed.
     *
     * @param id the deed ID
     * @param deed the updated deed data
     * @return optional containing the updated deed if found
     */
    public Optional<Deed> updateDeed(String id, Deed deed) {
        return deedRepository.findById(id).map(existingDeed -> {
            if (deed.getTitle() != null) {
                existingDeed.setTitle(deed.getTitle());
            }
            if (deed.getMatter() != null) {
                existingDeed.setMatter(deed.getMatter());
            }
            if (deed.getType() != null) {
                existingDeed.setType(deed.getType());
            }
            if (deed.getStatus() != null) {
                existingDeed.setStatus(deed.getStatus());
            }
            if (deed.getParties() != null) {
                // Generate IDs for new parties added during update
                for (Party party : deed.getParties()) {
                    if (party.getId() == null || party.getId().trim().isEmpty()) {
                        party.setId(partyIdGenerator.generatePartyId());
                        LocalDateTime now = LocalDateTime.now();
                        party.setCreatedAt(now);
                        party.setUpdatedAt(now);
                    }

                    // Generate IDs for any new attached documents
                    if (party.getDocuments() != null) {
                        for (Document doc : party.getDocuments()) {
                            if (doc.getId() == null || doc.getId().trim().isEmpty()) {
                                doc.setId(documentIdGenerator.generateDocumentId());
                            }
                        }
                    }
                }
                existingDeed.setParties(deed.getParties());
            }
            existingDeed.setUpdatedAt(LocalDateTime.now());
            return deedRepository.save(existingDeed);
        });
    }

    /**
     * Delete a deed by ID.
     *
     * @param id the deed ID
     */
    public void deleteDeed(String id) {
        deedRepository.deleteById(id);
    }

    /**
     * Get deeds by type.
     *
     * @param type the deed type
     * @return list of deeds of the specified type
     */
    public List<Deed> getDeedsByType(DeedType type) {
        return deedRepository.findByType(type);
    }

    /**
     * Search deeds by title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of deeds with matching title
     */
    public List<Deed> searchDeedsByTitle(String title) {
        return deedRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Get a deed by exact title match.
     *
     * @param title the exact title
     * @return optional containing the deed if found
     */
    public Optional<Deed> getDeedByTitle(String title) {
        return deedRepository.findByTitle(title);
    }

    /**
     * Get deeds by type and search term in title.
     *
     * @param type the deed type
     * @param title the title search term
     * @return list of deeds matching both criteria
     */
    public List<Deed> getDeedsByTypeAndTitle(DeedType type, String title) {
        return deedRepository.findByTypeAndTitleContainingIgnoreCase(type, title);
    }

    /**
     * Update a specific party within a deed.
     * Takes a Deed ID and Party ID, and updates the party information in MongoDB.
     *
     * @param deedId the deed ID
     * @param partyId the party ID to update
     * @param updatedPartyData the updated party data
     * @return optional containing the updated deed if found
     */
    public Optional<Deed> updatePartyInDeed(String deedId, String partyId, Party updatedPartyData) {
        return deedRepository.findById(deedId).map(deed -> {
            if (deed.getParties() != null) {
                // Find the party with the matching ID
                Optional<Party> partyToUpdate = deed.getParties().stream()
                        .filter(party -> party.getId().equals(partyId))
                        .findFirst();

                if (partyToUpdate.isPresent()) {
                    Party party = partyToUpdate.get();
                    
                    // Update party fields if provided
                    if (updatedPartyData.getName() != null) {
                        party.setName(updatedPartyData.getName());
                    }
                    if (updatedPartyData.getEmailId() != null) {
                        party.setEmailId(updatedPartyData.getEmailId());
                    }
                    if (updatedPartyData.getPhoneNumber() != null) {
                        party.setPhoneNumber(updatedPartyData.getPhoneNumber());
                    }

                    if (updatedPartyData.getPartyType() != null) {
                        party.setPartyType(updatedPartyData.getPartyType());
                    }

                    if (updatedPartyData.getDocuments() != null) {
                        for (Document doc : updatedPartyData.getDocuments()) {
                            if (doc.getId() == null || doc.getId().trim().isEmpty()) {
                                doc.setId(documentIdGenerator.generateDocumentId());
                            }
                        }
                        party.setDocuments(updatedPartyData.getDocuments());
                    }
                    
                    // Update party's updatedAt timestamp
                    party.setUpdatedAt(LocalDateTime.now());
                    
                    // Update deed's updatedAt timestamp
                    deed.setUpdatedAt(LocalDateTime.now());
                    
                    // Save and return the updated deed
                    return deedRepository.save(deed);
                }
            }
            return deed;
        });
    }

    /**
     * Add a party to a deed.
     *
     * @param deedId the deed ID
     * @param party the party to add
     * @return optional containing the updated deed if found
     */
    public Optional<Deed> addPartyToDeed(String deedId, Party party) {
        return deedRepository.findById(deedId).map(deed -> {
            if (deed.getParties() != null) {
                // Ensure party has an ID
                if (party.getId() == null || party.getId().trim().isEmpty()) {
                    party.setId(partyIdGenerator.generatePartyId());
                    LocalDateTime now = LocalDateTime.now();
                    party.setCreatedAt(now);
                    party.setUpdatedAt(now);
                }

                if (party.getDocuments() != null) {
                    for (Document doc : party.getDocuments()) {
                        if (doc.getId() == null || doc.getId().trim().isEmpty()) {
                            doc.setId(documentIdGenerator.generateDocumentId());
                        }
                    }
                }

                // Check if party with this ID doesn't already exist
                boolean partyExists = deed.getParties().stream()
                        .anyMatch(p -> p.getId().equals(party.getId()));
                
                if (!partyExists) {
                    deed.getParties().add(party);
                    deed.setUpdatedAt(LocalDateTime.now());
                    return deedRepository.save(deed);
                }
            }
            return deed;
        });
    }

    /**
     * Remove a party from a deed.
     *
     * @param deedId the deed ID
     * @param partyId the party ID to remove
     * @return optional containing the updated deed if found
     */
    public Optional<Deed> removePartyFromDeed(String deedId, String partyId) {
        return deedRepository.findById(deedId).map(deed -> {
            if (deed.getParties() != null) {
                deed.getParties().removeIf(party -> party.getId().equals(partyId));
                deed.setUpdatedAt(LocalDateTime.now());
                return deedRepository.save(deed);
            }
            return deed;
        });
    }

    /**
     * Get all parties for a deed.
     *
     * @param deedId the deed ID
     * @return optional containing list of parties in the deed
     */
    public Optional<List<Party>> getPartiesForDeed(String deedId) {
        return deedRepository.findById(deedId).map(Deed::getParties);
    }

    /**
     * Get a specific party from a deed.
     *
     * @param deedId the deed ID
     * @param partyId the party ID
     * @return optional containing the party if found
     */
    public Optional<Party> getPartyFromDeed(String deedId, String partyId) {
        return deedRepository.findById(deedId).flatMap(deed -> {
            if (deed.getParties() != null) {
                return deed.getParties().stream()
                        .filter(party -> party.getId().equals(partyId))
                        .findFirst();
            }
            return Optional.empty();
        });
    }

    /**
     * Get deeds by status.
     *
     * @param status the deed status
     * @return list of deeds with the specified status
     */
    public List<Deed> getDeedsByStatus(DeedStatus status) {
        return deedRepository.findByStatus(status);
    }

    /**
     * Get deeds by type and status.
     *
     * @param type the deed type
     * @param status the deed status
     * @return list of deeds matching both criteria
     */
    public List<Deed> getDeedsByTypeAndStatus(DeedType type, DeedStatus status) {
        return deedRepository.findByTypeAndStatus(type, status);
    }

    /**
     * Get deeds by status and search term in title.
     *
     * @param status the deed status
     * @param title the title search term
     * @return list of deeds matching both criteria
     */
    public List<Deed> getDeedsByStatusAndTitle(DeedStatus status, String title) {
        return deedRepository.findByStatusAndTitleContainingIgnoreCase(status, title);
    }

    /**
     * Update deed status.
     *
     * @param deedId the deed ID
     * @param newStatus the new status
     * @return optional containing the updated deed if found
     */
    public Optional<Deed> updateDeedStatus(String deedId, DeedStatus newStatus) {
        return deedRepository.findById(deedId).map(deed -> {
            deed.setStatus(newStatus);
            deed.setUpdatedAt(LocalDateTime.now());
            return deedRepository.save(deed);
        });
    }

    /**
     * Find deeds by party name.
     *
     * @param partyName the party name to search for
     * @return list of deeds containing a party with the specified name
     */
    public List<Deed> getDeedsByPartyName(String partyName) {
        return deedRepository.findByPartyName(partyName);
    }

    /**
     * Find deeds by party ID.
     *
     * @param partyId the party ID to search for
     * @return list of deeds containing the specified party ID
     */
    public List<Deed> getDeedsByPartyId(String partyId) {
        return deedRepository.findByPartyId(partyId);
    }

    /**
     * Find deeds by party phone number.
     *
     * @param phoneNumber the phone number to search for
     * @return list of deeds containing a party with the specified phone number
     */
    public List<Deed> getDeedsByPartyPhoneNumber(String phoneNumber) {
        return deedRepository.findByPartyPhoneNumber(phoneNumber);
    }

    /**
     * Find deeds by party ID type.
     *
     * @param idType the ID type of the party
     * @return list of deeds containing a party with the specified ID type
     */
    public List<Deed> getDeedsByPartyIdType(IdType idType) {
        return deedRepository.findByPartyIdType(idType);
    }
}