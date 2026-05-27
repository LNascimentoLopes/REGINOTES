import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginResponse {
  Token: string;
  refreshToken: string;

}
export interface LoginRequest {
  email: string;
  password: string;
}
const API_URL = environment.apiUrl;
@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private readonly apiUrl = `${API_URL}/auth/login`;
  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    const request: LoginRequest = { email, password };
    return this.http.post<LoginResponse>(this.apiUrl, request);
  }
  
}

