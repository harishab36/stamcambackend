package org.fp.stamcam.services;

import org.fp.stamcam.models.Role;
import org.fp.stamcam.models.User;
import org.fp.stamcam.repositories.RoleRepository;
import org.fp.stamcam.repositories.UserRepository;
import org.fp.stamcam.utils.UserIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserIdGenerator userIdGenerator;

    public User createUser(User user) {
        user.setId(userIdGenerator.generateUserId());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (user.getRoleIds() == null) {
            user.setRoleIds(new ArrayList<>());
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> updateUser(String id, User userDetails) {
        return userRepository.findById(id).map(existing -> {
            if (userDetails.getUsername() != null) {
                existing.setUsername(userDetails.getUsername());
            }
            if (userDetails.getPassword() != null) {
                existing.setPassword(userDetails.getPassword());
            }
            if (userDetails.getRoleIds() != null) {
                existing.setRoleIds(userDetails.getRoleIds());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existing);
        });
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    // -------------------------------------------------------------------------
    // Role assignment
    // -------------------------------------------------------------------------

    /**
     * Assign a role to a user. No-op if the role is already assigned.
     * Returns empty if either the user or role does not exist.
     */
    public Optional<User> assignRoleToUser(String userId, String roleId) {
        if (!roleRepository.existsById(roleId)) {
            return Optional.empty();
        }
        return userRepository.findById(userId).map(user -> {
            if (!user.getRoleIds().contains(roleId)) {
                user.getRoleIds().add(roleId);
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            }
            return user;
        });
    }

    /**
     * Remove a role from a user. Returns empty if the user does not exist.
     */
    public Optional<User> removeRoleFromUser(String userId, String roleId) {
        return userRepository.findById(userId).map(user -> {
            user.getRoleIds().remove(roleId);
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        });
    }

    /**
     * Replace all roles of a user with the provided role IDs.
     * Ignores IDs that do not correspond to existing roles.
     */
    public Optional<User> setUserRoles(String userId, List<String> roleIds) {
        List<String> validIds = roleRepository.findAllById(roleIds)
                .stream().map(Role::getId).toList();
        return userRepository.findById(userId).map(user -> {
            user.setRoleIds(new ArrayList<>(validIds));
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        });
    }

    /**
     * Resolve and return the full Role objects assigned to a user.
     */
    public Optional<List<Role>> getUserRoles(String userId) {
        return userRepository.findById(userId).map(user ->
                roleRepository.findAllById(user.getRoleIds())
        );
    }

    // -------------------------------------------------------------------------
    // Role-based user queries
    // -------------------------------------------------------------------------

    public List<User> getUsersByRoleId(String roleId) {
        return userRepository.findByRoleIdsContaining(roleId);
    }

    public List<User> getUsersByAllRoleIds(List<String> roleIds) {
        return userRepository.findByRoleIdsContainingAll(roleIds);
    }

    public List<User> getUsersByAnyRoleId(List<String> roleIds) {
        return userRepository.findByRoleIdsIn(roleIds);
    }

    public List<User> getUsersWithNoRoles() {
        return userRepository.findUsersWithNoRoles();
    }

    public long countUsersByRoleId(String roleId) {
        return userRepository.countByRoleIdsContaining(roleId);
    }
}
    