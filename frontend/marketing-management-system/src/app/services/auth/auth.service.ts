import { EventEmitter, Injectable } from '@angular/core';
import jwtDecode from 'jwt-decode';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiHost: string = 'https://localhost:8443';
  constructor(private http: HttpClient) {
    this.scheduleTokenRefresh();
  }

  private readonly tokenAccessKey = 'jwt-access-token';
  private readonly tokenRefreshKey = 'jwt-refresh-token';

  onLogout: EventEmitter<void> = new EventEmitter<void>();
  onLogin: EventEmitter<void> = new EventEmitter<void>();
  private accessTokenIssuedAt: Date | null = null;

  setToken(accessToken: string, refreshToken: string): void {
    localStorage.setItem(this.tokenAccessKey, accessToken);
    localStorage.setItem(this.tokenRefreshKey, refreshToken);
    this.accessTokenIssuedAt = new Date();
    this.onLogin.emit();
  }

  getUserRole() {
    var jwt;
    if (this.getToken()) {
      jwt = jwtDecode(this.getToken()!) as any;
    } else {
      return '';
    }
    return jwt.role;
  }

  getToken(): string {
    var token = localStorage.getItem(this.tokenAccessKey); 
    if( token != null)
      return token;
    return "" 
  }

  removeToken(): void {
    localStorage.removeItem(this.tokenAccessKey);
    localStorage.removeItem(this.tokenRefreshKey)
    this.onLogout.emit();
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token;
  }

  refreshAccessToken(): void {
    if (!this.accessTokenIssuedAt) {
      console.error('Access token issue time not found.');
      return;
    }
  
   
    const expiresInMilliseconds = 15 * 60 * 1000; 
    const expiresAt = new Date(this.accessTokenIssuedAt.getTime() + expiresInMilliseconds);
  
    if (expiresAt <= new Date()) {
      console.error('Access token has expired.');
      return;
    }
  
    const refreshToken = localStorage.getItem(this.tokenRefreshKey);
  
    if (!refreshToken) {
      console.error('Refresh token not found.');
      return;
    }
  
    const refreshTokenEndpoint = this.apiHost +'/refresh-token'; 
  
    this.http.post<any>(refreshTokenEndpoint, null, {
      headers: {
        'Authorization': `Bearer ${refreshToken}`
      }
    }).subscribe(
      data => {
        const newAccessToken = data.accessToken;
        this.setToken(newAccessToken, refreshToken);
      },
      error => {
        console.error('Error refreshing access token:', error);
        if (error.status === 401) {
         
          this.removeToken();
        }
      }
    );
  }
  
  scheduleTokenRefresh() {
    setInterval(() => {
      this.refreshAccessToken();
    }, 14 * 60 * 1000); 
  }
}
