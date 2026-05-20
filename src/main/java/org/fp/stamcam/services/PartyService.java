package org.fp.stamcam.services;

import org.fp.stamcam.models.Party;
import org.fp.stamcam.models.PartyType;
import org.fp.stamcam.models.IdType;
import org.fp.stamcam.repositories.PartyRepository;
import org.fp.stamcam.utils.PartyIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Party operations.
 * Contains business logic for creating, retrieving, updating, and deleting parties.
 */
@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyIdGenerator partyIdGenerator;

    /**
     * Create a new party.
     *
     * @param party the party to create
     * @return the created party with generated ID and timestamps
     */
    public Party createParty(Party party) {
        party.setId(partyIdGenerator.generatePartyId());
        party.setCreatedAt(LocalDateTime.now());
        party.setUpdatedAt(LocalDateTime.now());
        return partyRepository.save(party);
    }

    /**
     * Get a party by ID.
     *
     * @param id the party ID
     * @return optional containing the party if found
     */
    public Optional<Party> getPartyById(String id) {
        return partyRepository.findById(id);
    }

    /**
     * Get all parties.
     *
     * @return list of all parties
     */
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    /**
     * Update an existing party.
     *
     * @param id the party ID
     * @param partyDetails the updated party details
     * @return optional containing the updated party if found
     */
    public Optional<Party> updateParty(String id, Party partyDetails) {
        return partyRepository.findById(id).map(existingParty -> {
            if (partyDetails.getName() != null) {
                existingParty.setName(partyDetails.getName());
            }
            if (partyDetails.getEmailId() != null) {
                existingParty.setEmailId(partyDetails.getEmailId());
            }
            if (partyDetails.getPhoneNumber() != null) {
                existingParty.setPhoneNumber(partyDetails.getPhoneNumber());
            }
            if (partyDetails.getIdType() != null) {
                existingParty.setIdType(partyDetails.getIdType());
            }
            if (partyDetails.getPartyType() != null) {
                existingParty.setPartyType(partyDetails.getPartyType());
            }
            existingParty.setUpdatedAt(LocalDateTime.now());
            return partyRepository.save(existingParty);
        });
    }

    /**
     * Delete a party by ID.
     *
     * @param id the party ID
     * @return true if deleted, false if not found
     */
    public boolean deleteParty(String id) {
        if (partyRepository.existsById(id)) {
            partyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get all parties by party type.
     *
     * @param partyType the party type
     * @return list of parties with the specified type
     */
    public List<Party> getPartiesByType(PartyType partyType) {
        return partyRepository.findByPartyType(partyType);
    }

    /**
     * Get all parties by identification type.
     *
     * @param idType the identification type
     * @return list of parties with the specified ID type
     */
    public List<Party> getPartiesByIdType(IdType idType) {
        return partyRepository.findByIdType(idType);
    }

    /**
     * Get a party by email ID.
     *
     * @param emailId the email ID
     * @return optional containing the party if found
     */
    public Optional<Party> getPartyByEmailId(String emailId) {
        return partyRepository.findByEmailId(emailId);
    }

    /**
     * Search for parties by name.
     *
     * @param nameSearchTerm the search term
     * @return list of matching parties
     */
    public List<Party> searchPartiesByName(String nameSearchTerm) {
        return partyRepository.findByNameContainingIgnoreCase(nameSearchTerm);
    }

}