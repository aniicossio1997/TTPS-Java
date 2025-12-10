import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginResponse, Usuario } from '../interfaces/LoginResponse.interface';



@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);

  private readonly apiUrl = `${environment.API_URL}/auth`;

  login(email: string, password: string): Observable<LoginResponse> {
    //    // esto pega a http://localhost:4002/api/auth/login y el proxy lo manda a http://localhost:8081/auth/login

    const headers = new HttpHeaders({
      'accept': '*/*',
      'usuario': email,
      'password': password
    });

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, null, { headers });
  }

}
