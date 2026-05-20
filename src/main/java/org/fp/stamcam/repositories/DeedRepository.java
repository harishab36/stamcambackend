package org.fp.stamcam.repositories;

import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.DeedType;
import org.fp.stamcam.models.DeedStatus;
import org.fp.stamcam.models.IdType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for Deed entity.
 * Provides CRUD operations and custom query methods for Deed documents.
 */
@Repository
public interface DeedRepository extends MongoRepository<Deed, String> {

    /**
     * Find deeds by type.
     *
     * @param type the deed type to search for
     * @return list of deeds of the specified type
     */
    @Query("{ 'type' : ?0 }")
    List<Deed> findByType(DeedType type);

    /**
     * Find deeds by title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of deeds with matching title
     */
    @Query("{ 'title' : { $regex: ?0, $options: 'i' } }")
    List<Deed> findByTitleContainingIgnoreCase(String title);

    /**
     * Find a deed by title (exact match).
     *
     * @param title the exact title to search for
     * @return optional containing the deed if found
     */
    @Query("{ 'title' : ?0 }")
    Optional<Deed> findByTitle(String title);

    /**
     * Find deeds by type and search term in title.
     *
     * @param type the deed type
     * @param title the title to search for
     * @return list of deeds matching both criteria
     */
    @Query("{ 'type' : ?0, 'title' : { $regex: ?1, $options: 'i' } }")
    List<Deed> findByTypeAndTitleContainingIgnoreCase(DeedType type, String title);

    /**
     * Find deeds by status.
     *
     * @param status the deed status to search for
     * @return list of deeds with the specified status
     */
    @Query("{ 'status' : ?0 }")
    List<Deed> findByStatus(DeedStatus status);

    /**
     * Find deeds by type and status.
     *
     * @param type the deed type
     * @param status the deed status
     * @return list of deeds matching both criteria
     */
    @Query("{ 'type' : ?0, 'status' : ?1 }")
    List<Deed> findByTypeAndStatus(DeedType type, DeedStatus status);

    /**
     * Find deeds by status and search term in title.
     *
     * @param status the deed status
     * @param title the title to search for
     * @return list of deeds matching both criteria
     */
    @Query("{ 'status' : ?0, 'title' : { $regex: ?1, $options: 'i' } }")
    List<Deed> findByStatusAndTitleContainingIgnoreCase(DeedStatus status, String title);

    /**
     * Find deeds by party name.
     *
     * @param partyName the party name to search for
     * @return list of deeds containing a party with the specified name
     */
    @Query("{ 'parties.name' : { $regex: ?0, $options: 'i' } }")
    List<Deed> findByPartyName(String partyName);

    /**
     * Find deeds by party ID.
     *
     * @param partyId the party ID to search for
     * @return list of deeds containing the specified party ID
     */
    @Query("{ 'parties._id' : ?0 }")
    List<Deed> findByPartyId(String partyId);

    /**
     * Find deeds by party phone number.
     *
     * @param phoneNumber the phone number to search for
     * @return list of deeds containing a party with the specified phone number
     */
    @Query("{ 'parties.phoneNumber' : ?0 }")
    List<Deed> findByPartyPhoneNumber(String phoneNumber);
    
    /**
     * Find deeds by party ID type.
     * 
     * @param idType the ID type of the party
     * @return list of deeds containing a party with the specified ID type
     */
    @Query("{ 'parties.idType' : ?0 }")
    List<Deed> findByPartyIdType(IdType idType);

}
