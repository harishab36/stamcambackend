package org.fp.stamcam.services;

import org.fp.stamcam.models.Role;
import org.fp.stamcam.models.Screen;
import org.fp.stamcam.repositories.RoleRepository;
import org.fp.stamcam.utils.RoleIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleIdGenerator roleIdGenerator;

    public Role createRole(Role role) {
        role.setId(roleIdGenerator.generateRoleId());
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(String id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<Role> searchRolesByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    public boolean roleNameExists(String name) {
        return roleRepository.existsByName(name);
    }

    public List<Role> getRolesByScreenName(String screenName) {
        return roleRepository.findByAllowedScreenName(screenName);
    }

    public List<Role> getRolesByScreenRoute(String screenRoute) {
        return roleRepository.findByAllowedScreenRoute(screenRoute);
    }

    public List<Role> searchRolesByScreenName(String screenName) {
        return roleRepository.findByAllowedScreenNameContainingIgnoreCase(screenName);
    }

    public List<Role> getRolesBySectionName(String sectionName) {
        return roleRepository.findByAllowedSectionName(sectionName);
    }

    public List<Role> searchRolesBySectionName(String sectionName) {
        return roleRepository.findByAllowedSectionNameContainingIgnoreCase(sectionName);
    }

    public List<Role> getRolesByScreenAndSection(String screenName, String sectionName) {
        return roleRepository.findByScreenNameAndSectionName(screenName, sectionName);
    }

    public List<Role> getRolesWithScreens() {
        return roleRepository.findRolesWithScreens();
    }

    public List<Role> getRolesWithoutScreens() {
        return roleRepository.findRolesWithoutScreens();
    }

    public long countRolesByScreenName(String screenName) {
        return roleRepository.countByAllowedScreenName(screenName);
    }

    public Optional<Role> updateRole(String id, Role roleDetails) {
        return roleRepository.findById(id).map(existing -> {
            if (roleDetails.getName() != null) {
                existing.setName(roleDetails.getName());
            }
            if (roleDetails.getAllowedScreens() != null) {
                existing.setAllowedScreens(roleDetails.getAllowedScreens());
            }
            return roleRepository.save(existing);
        });
    }

    public Optional<Role> addScreenToRole(String id, Screen screen) {
        return roleRepository.findById(id).map(existing -> {
            List<Screen> screens = existing.getAllowedScreens();
            screens.add(screen);
            existing.setAllowedScreens(screens);
            return roleRepository.save(existing);
        });
    }

    public Optional<Role> removeScreenFromRole(String id, String screenName) {
        return roleRepository.findById(id).map(existing -> {
            existing.getAllowedScreens().removeIf(s -> s.getName().equals(screenName));
            return roleRepository.save(existing);
        });
    }

    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }
}