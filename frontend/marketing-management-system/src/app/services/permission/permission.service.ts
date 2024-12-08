import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  private apiHost: string = 'https://localhost:8443/permission';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private createHeaders(): HttpHeaders {
    const token = this.auth.getToken();
    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    });
  }

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiHost, {
      headers: this.createHeaders(),
    });
  }

  changePermission(roleName: string, id: any): Observable<any> {
    const body = {
      roleName: roleName,
      permissionId: id,
    };

    return this.http.put<any>(this.apiHost, body, {
      headers: this.createHeaders(),
    });
  }
}
