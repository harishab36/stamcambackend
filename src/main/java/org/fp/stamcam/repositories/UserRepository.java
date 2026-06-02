package org.fp.stamcam.repositories;

import org.fp.stamcam.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for User entity.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    /**
     * Find all users that have the given role ID in their roleIds list.
     */
    List<User> findByRoleIdsContaining(String roleId);

    /**
     * Find users that have all of the given role IDs.
     */
    @Query("{ 'roleIds': { $all: ?0 } }")
    List<User> findByRoleIdsContainingAll(List<String> roleIds);

    /**
     * Find users that have any of the given role IDs.
     */
    @Query("{ 'roleIds': { $in: ?0 } }")
    List<User> findByRoleIdsIn(List<String> roleIds);

    /**
     * Find users that have no roles assigned.
     */
    @Query("{ $or: [ { 'roleIds': { $exists: false } }, { 'roleIds': { $size: 0 } } ] }")
    List<User> findUsersWithNoRoles();

    /**
     * Count users assigned to a specific role.
     */
    long countByRoleIdsContaining(String roleId);
}
