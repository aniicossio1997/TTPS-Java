import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UbicacionExternaResponse } from '../interfaces/ubicacionExternaResponse';

@Injectable({
  providedIn: 'root',
})
export class GeorefService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.API_URL}/georef`;
  constructor() { }

  getProvincias(): Observable<UbicacionExternaResponse> {
    return this.http.get<UbicacionExternaResponse>(`${this.apiUrl}/provincias`);
  }

  getDepartamentos(idProvincia:number): Observable<UbicacionExternaResponse> {
    return this.http.get<UbicacionExternaResponse>(`${this.apiUrl}/departamentos/${idProvincia}`);
  }

  getMunicipios(idProvincia:number): Observable<UbicacionExternaResponse> {
    return this.http.get<UbicacionExternaResponse>(`${this.apiUrl}/municipios/${idProvincia}`);
  }



}
