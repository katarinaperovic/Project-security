package marketing.management.system.service.implementations;

import marketing.management.system.exception.PermissionException;
import marketing.management.system.model.Permission;
import marketing.management.system.model.Role;
import marketing.management.system.repository.PermissionRepository;
import marketing.management.system.service.interfaces.PermissionService;
import marketing.management.system.service.interfaces.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;
    private static final Logger logger = LogManager.getLogger(PermissionServiceImpl.class);



    @Autowired
    RoleService roleService;

    @Override
    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    @Override
    public boolean changePermissionForRole(String permissionId, String roleName)  throws PermissionException {
        Permission permission = getPermissionById(permissionId);
        Role role = getRoleByName(roleName);
        List<Role> roles = permission.getRoles();

        boolean hasPermissionBefore = isRoleInPermission(permission, roleName);

        updateRolesList(permission, role, roles);
        savePermission(permission);

        boolean hasPermissionAfter = isRoleInPermission(permission, roleName);

        if (hasPermissionBefore && !hasPermissionAfter) {
            logger.info("Permission '{}' has been removed for role '{}'.", permission.getDescription(), roleName);
        } else if (!hasPermissionBefore && hasPermissionAfter) {
            logger.info("Permission '{}' has been granted to role '{}'.", permission.getDescription(), roleName);
        } else {
            logger.info("Permission '{}' has been updated for role '{}'.", permission.getDescription(), roleName);
        }

        return true;
    }



    @Override
    public boolean checkPermission(String permissionName) throws PermissionException {
        Permission permission = permissionRepository.findByDescription(permissionName);

    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean hasPermission = checkUserPermission(userDetails, permission);

        if (hasPermission) {
        return true;
    } else {
            PermissionException permissionException = new PermissionException("User does not have permission for " + permissionName);
            permissionException.setPermissionName(permissionName);
            permissionException.setUser(userDetails.getUsername());
            throw permissionException;}
}

private boolean checkUserPermission(UserDetails userDetails, Permission permission) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    for (GrantedAuthority authority : authorities) {
        String role = authority.getAuthority();

        if (isRoleInPermission(permission, role)) {
            return true;
        }
    }

    return false;
}

private boolean isRoleInPermission(Permission permission, String role) {
    return permission.getRoles().stream().anyMatch(r -> r.getName().equals(role));
}
    private Permission getPermissionById(String permissionId) {
        Long id = Long.valueOf(permissionId);
        return permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
    }

    private Role getRoleByName(String roleName) {
        Role role = roleService.findByName(roleName);
        if (role == null){
            throw new IllegalArgumentException("Role not found");
        }
        return role;
    }

    private void updateRolesList(Permission permission, Role role, List<Role> roles) {
        if (roles.contains(role)) {
            roles.remove(role);
        } else {
            roles.add(role);
        }
        permission.setRoles(roles);
    }

    private void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }
}
