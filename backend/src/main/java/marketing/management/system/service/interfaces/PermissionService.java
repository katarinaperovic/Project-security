package marketing.management.system.service.interfaces;

import marketing.management.system.exception.PermissionException;
import marketing.management.system.model.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAll();

    boolean changePermissionForRole(String permissionId, String roleName);
    boolean checkPermission(String permissionName) throws PermissionException;
}
