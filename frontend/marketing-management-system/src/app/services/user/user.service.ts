import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  apiHost: string = 'https://localhost:8443/auth/';
  headers: HttpHeaders = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient) {}

  register(user: User): Observable<User> {
    
    return this.http.post<User>(this.apiHost + 'register', JSON.stringify(user), {
      headers: this.headers,
    });
  }

  loginUser(user: User,recaptchaToken:string): Observable<any> {
    const body = {
      'username': user.email,
      'password': user.password,
      'recaptchaToken':recaptchaToken
    };

    const headersWithEmail = this.headers.set('X-User-Email', user.email);
    return this.http.post<User>(this.apiHost + 'login', JSON.stringify(body), {
      headers: headersWithEmail,
    });
  }
}
