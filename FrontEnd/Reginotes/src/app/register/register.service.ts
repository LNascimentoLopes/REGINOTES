import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface registerRequest {
  username: string;
  email: string;
  password: string;
}
@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private readonly API_URL = environment.apiUrl;
  private readonly apiUrl = `${this.API_URL}/auth/register`;
  constructor(private http: HttpClient) {}

  register(username: string, email: string, password: string): Observable<any> {
    const request: registerRequest = { username, email, password };
    return this.http.post(this.apiUrl, request,  { responseType: 'text' });
  }
}

