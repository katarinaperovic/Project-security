import { Component, OnInit } from '@angular/core';
import { PermissionService } from 'src/app/services/permission/permission.service';

@Component({
  selector: 'app-permission',
  templateUrl: './permission.component.html',
  styleUrls: ['./permission.component.css'],
})
export class PermissionComponent implements OnInit {

  public permissions: any[] = [];

  constructor(private permissionService: PermissionService) {}

  ngOnInit(): void {
    this.permissionService.getAll().subscribe(
      (res) => {
        this.permissions = res;
      },
      (error) => {}
    );
  }


  hasRole(permission: any, roleName: string): boolean {
    return permission.roles.some((role: any) => role.name === roleName);
  }

  toggleRole(permission: any, roleName: string): void {
    const roleIndex = permission.roles.findIndex(
        (role: any) => role.name === roleName
      );
  
      this.permissionService
        .changePermission(roleName, permission.id)
        .subscribe();
    if (roleIndex !== -1) {
      permission.roles.splice(roleIndex, 1);
    } else {
      permission.roles.push({ name: roleName });
    }
  }
}