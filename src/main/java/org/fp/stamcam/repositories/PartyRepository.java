package org.fp.stamcam.repositories;

import org.fp.stamcam.models.Party;
import org.fp.stamcam.models.PartyType;
import org.fp.stamcam.models.IdType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Party entity.
 * Provides database operations for Party documents.
 */
@Repository
public interface PartyRepository extends MongoRepository<Party, String> {

    /**
     * Find all parties by party type.
     *
     * @param partyType the party type
     * @return list of parties with the specified type
     */
    List<Party> findByPartyType(PartyType partyType);

    /**
     * Find a party by name (case-insensitive).
     *
     * @param name the party name
     * @return optional containing the party if found
     */
    Optional<Party> findByNameIgnoreCase(String name);

    /**
     * Find a party by email ID.
     *
     * @param emailId the email ID
     * @return optional containing the party if found
     */
    Optional<Party> findByEmailId(String emailId);

    /**
     * Search for parties by name containing the search term (case-insensitive).
     *
     * @param nameSearchTerm the search term
     * @return list of matching parties
     */
    List<Party> findByNameContainingIgnoreCase(String nameSearchTerm);

}