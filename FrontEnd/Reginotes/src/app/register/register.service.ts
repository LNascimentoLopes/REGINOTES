import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';

export interface registerRequest {
  username: string;
  email: string;
  password: string;
}
@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private readonly apiUrl = 'http://localhost:8080/auth/register';
  constructor(private http: HttpClient) {}

  register(username: string, email: string, password: string): Observable<any> {
    const request: registerRequest = { username, email, password };
    return this.http.post(this.apiUrl, request,  { responseType: 'text' });
  }
}

