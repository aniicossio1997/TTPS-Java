import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { UbicacionExternaResponse } from '../interfaces/ubicacionExternaResponse';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GeorefApiExternaService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.URL_MAP}`;
  constructor() { }

  getUbicacionExterna(lat: string, lon: string): Observable<UbicacionExternaResponse> {

    return this.http.get<UbicacionExternaResponse>(`${this.apiUrl}/ubicacion?lat=${lat}&lon=${lon}`);
  }

}
