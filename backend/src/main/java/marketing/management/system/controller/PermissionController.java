package marketing.management.system.controller;

import marketing.management.system.dto.PermissionForRoleDTO;
import marketing.management.system.model.Permission;
import marketing.management.system.service.interfaces.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> updatePermission() {
        return ResponseEntity.ok(permissionService.getAll());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> changePermissionForRole(@RequestBody PermissionForRoleDTO dto) {
        permissionService.changePermissionForRole(dto.getPermissionId(), dto.getRoleName());
        return ResponseEntity.ok(true);
    }
}
