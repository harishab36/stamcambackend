package org.fp.stamcam.repositories;

import org.fp.stamcam.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for Role entity.
 * Provides CRUD operations and custom query methods for Role documents.
 */
@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    /**
     * Find a role by exact name.
     */
    Optional<Role> findByName(String name);

    /**
     * Find roles whose name contains the given string (case-insensitive).
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Role> findByNameContainingIgnoreCase(String name);

    /**
     * Check whether a role with the given name already exists.
     */
    boolean existsByName(String name);

    /**
     * Find roles that have access to a screen identified by its name.
     */
    @Query("{ 'allowedScreens': { $elemMatch: { 'name': ?0 } } }")
    List<Role> findByAllowedScreenName(String screenName);

    /**
     * Find roles that have access to a screen identified by its route.
     */
    @Query("{ 'allowedScreens': { $elemMatch: { 'route': ?0 } } }")
    List<Role> findByAllowedScreenRoute(String screenRoute);

    /**
     * Find roles that have access to a screen whose name matches a pattern (case-insensitive).
     */
    @Query("{ 'allowedScreens': { $elemMatch: { 'name': { $regex: ?0, $options: 'i' } } } }")
    List<Role> findByAllowedScreenNameContainingIgnoreCase(String screenName);

    /**
     * Find roles that have a specific section name in any of their allowed screens.
     */
    @Query("{ 'allowedScreens.allowedSections': { $elemMatch: { 'name': ?0 } } }")
    List<Role> findByAllowedSectionName(String sectionName);

    /**
     * Find roles that have a specific section name (case-insensitive) in any of their allowed screens.
     */
    @Query("{ 'allowedScreens.allowedSections': { $elemMatch: { 'name': { $regex: ?0, $options: 'i' } } } }")
    List<Role> findByAllowedSectionNameContainingIgnoreCase(String sectionName);

    /**
     * Find roles that have access to both a specific screen and a specific section within that screen.
     */
    @Query("{ 'allowedScreens': { $elemMatch: { 'name': ?0, 'allowedSections': { $elemMatch: { 'name': ?1 } } } } }")
    List<Role> findByScreenNameAndSectionName(String screenName, String sectionName);

    /**
     * Find roles that have at least one allowed screen.
     */
    @Query("{ 'allowedScreens': { $exists: true, $not: { $size: 0 } } }")
    List<Role> findRolesWithScreens();

    /**
     * Find roles that have no allowed screens assigned.
     */
    @Query("{ $or: [ { 'allowedScreens': { $exists: false } }, { 'allowedScreens': { $size: 0 } } ] }")
    List<Role> findRolesWithoutScreens();

    /**
     * Count the number of roles that have access to a given screen.
     */
    @Query(value = "{ 'allowedScreens': { $elemMatch: { 'name': ?0 } } }", count = true)
    long countByAllowedScreenName(String screenName);
}