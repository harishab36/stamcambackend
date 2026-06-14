package org.fp.stamcam.repositories;

import org.fp.stamcam.models.DeedTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for DeedTitle entity.
 * Provides CRUD operations and custom query methods for DeedTitle documents.
 */
@Repository
public interface DeedTitleRepository extends MongoRepository<DeedTitle, String> {

    /**
     * Find deed titles by title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of deed titles with matching title
     */
    @Query("{ 'title' : { $regex: ?0, $options: 'i' } }")
    List<DeedTitle> findByTitleContainingIgnoreCase(String title);

    /**
     * Find a deed title by exact title match.
     *
     * @param title the exact title to search for
     * @return optional containing the deed title if found
     */
    @Query("{ 'title' : ?0 }")
    Optional<DeedTitle> findByTitle(String title);

}

