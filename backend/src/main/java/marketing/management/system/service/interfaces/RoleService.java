package marketing.management.system.service.interfaces;

import marketing.management.system.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(Long id);
    Role findByName(String name);
}
