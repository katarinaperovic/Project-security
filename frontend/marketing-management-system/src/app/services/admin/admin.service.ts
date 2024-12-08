import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  apiHost: string = 'https://localhost:8443/admin/';
  headers: HttpHeaders = new HttpHeaders({
    'Authorization': 'Bearer ' + this.auth.getToken(),
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient, private auth: AuthService) {}

  get(): Observable<any[]> {
    var jwttoken = 'ss'
    return this.http.get<any[]>(this.apiHost + 'getAll', {
      headers: this.headers,
    });
  }
}
