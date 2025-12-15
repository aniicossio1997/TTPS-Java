import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GeorefProvinciasResponse } from '../interfaces/georef/georefProvinciasResponse';
import { GeorefMunicipiosResponse } from '../interfaces/georef/georefMunicipiosResponse';
import { GeorefDepartamentosResponse } from '../interfaces/georef/georefDepartamentosResponse';

@Injectable({
  providedIn: 'root',
})
export class GeorefService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.API_URL}/georef`;
  constructor() { }

  getProvincias(): Observable<GeorefProvinciasResponse> {
    return this.http.get<GeorefProvinciasResponse>(`${this.apiUrl}/provincias`);
  }

  getDepartamentos(idProvincia:number): Observable<GeorefDepartamentosResponse> {
    return this.http.get<GeorefDepartamentosResponse>(`${this.apiUrl}/departamentos/${idProvincia}`);
  }

  getMunicipios(idProvincia:number): Observable<GeorefMunicipiosResponse> {
    return this.http.get<GeorefMunicipiosResponse>(`${this.apiUrl}/municipios/${idProvincia}`);
  }



}
