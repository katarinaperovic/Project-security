import { HttpHeaders, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  

  apiHost: string = 'https://localhost:8443/client';
  headers: HttpHeaders = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + this.auth.getToken()
  });

  constructor(private http: HttpClient, private auth: AuthService, private toast: ToastrService) {}

  getByID(id: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiHost}/${id}`, { headers: this.headers }).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.toast.error('Not authorized', 'Error');
        }
        return throwError(error);
      })
    );
  }

  
}
