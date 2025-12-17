import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../interfaces/LoginResponse.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { environment } from '../../environments/environment';


@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  private readonly apiUrl = `${environment.API_URL}/auth`;

  login(email: string, password: string): Observable<LoginResponse> {

    const headers = new HttpHeaders({
      'accept': '*/*',
      'usuario': email,
      'password': password
    });

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, null, { headers });
  }
  //http://localhost:4201/api/auth/login
  //http://localhost:8081


  //http://localhost:8081/api/auth/register

  postRegister(data: RegisterRequest) {
    // api/auth/register
    return this.http.post(`${this.apiUrl}/register`, data);
  }
}
