import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

interface ApiRequestOptions {
  headers?: HttpHeaders | { [header: string]: string | string[] };
  params?: { [param: string]: string | string[] };
}

export class ApiService {
  protected http = inject(HttpClient);

  protected readonly baseUrl = environment.API_URL;
  protected readonly basePath: string = '';

  protected get<T>(endpoint: string, options?: ApiRequestOptions): Observable<T> {
    const url = `${this.baseUrl}${this.basePath}${endpoint}`;
    console.log({ url }, environment.API_URL);
    return this.http.get<T>(url, options);
  }

  protected post<T, B>(endpoint: string, body: B, options?: ApiRequestOptions): Observable<T> {
    const url = `${this.baseUrl}${this.basePath}${endpoint}`;
    return this.http.post<T>(url, body, options);
  }

  protected put<T, B>(endpoint: string, body: B, options?: ApiRequestOptions): Observable<T> {
    const url = `${this.baseUrl}${this.basePath}${endpoint}`;
    return this.http.put<T>(url, body, options);
  }

  protected _delete<T>(endpoint: string, options?: ApiRequestOptions): Observable<T> {
    const url = `${this.baseUrl}${this.basePath}${endpoint}`;
    return this.http.delete<T>(url, options);
  }

  protected buildParams(parameters: Record<string, any>) {
    const params: { [key: string]: string | string[] } = {};
    Object.keys(parameters).forEach((key) => {
      const value = parameters[key];
      if (value !== undefined && value !== null) {
        params[key] = String(value);
      }
    });

    return params;
  }
}
