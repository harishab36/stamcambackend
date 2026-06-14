package org.fp.stamcam.repositories;

import org.fp.stamcam.models.PartyTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for PartyTitle entity.
 * Provides CRUD operations and custom query methods for PartyTitle documents.
 */
@Repository
public interface PartyTitleRepository extends MongoRepository<PartyTitle, String> {

    /**
     * Find party titles by title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of party titles with matching title
     */
    @Query("{ 'title' : { $regex: ?0, $options: 'i' } }")
    List<PartyTitle> findByTitleContainingIgnoreCase(String title);

    /**
     * Find a party title by exact title match.
     *
     * @param title the exact title to search for
     * @return optional containing the party title if found
     */
    @Query("{ 'title' : ?0 }")
    Optional<PartyTitle> findByTitle(String title);

}

