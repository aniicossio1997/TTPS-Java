import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import {
  Publicacion,
  PublicacionCreate,
  PublicacionFilter,
  PublicacionUpdate,
} from '../interfaces/publicacion.interface';
import { PaginatedResult } from '../interfaces/pagination.interface';

@Injectable({
  providedIn: 'root',
})
export class PublicacionesService extends ApiService {
  private readonly endpoint = '/publicaciones';

  public getAll(): Observable<Publicacion[]> {
    return this.get<Publicacion[]>(this.endpoint);
  }

  public getFiltered(filter: PublicacionFilter): Observable<PaginatedResult<Publicacion>> {
    const params = this.buildParams(filter);

    return this.get<PaginatedResult<Publicacion>>(this.endpoint, { params });
  }

  public getById(id: number): Observable<Publicacion> {
    return this.get<Publicacion>(`${this.endpoint}/${id}`);
  }

  public create(publicacion: PublicacionCreate, imagenes: File[] = []): Observable<Publicacion> {
     const formData = this.buildFormData(publicacion, imagenes);

    return this.http.post<Publicacion>(`${this.baseUrl}${this.endpoint}`, formData);
  }

  public update(id: number, publicacion: PublicacionUpdate, imagenes?: File[] ): Observable<Publicacion> {
     const formData = this.buildFormData(publicacion, imagenes);
    return this.http.put<Publicacion>(`${this.baseUrl}${this.endpoint}/${id}`, formData);
  }

  public delete(id: number): Observable<void> {
    return this._delete<void>(`${this.endpoint}/${id}`);
  }

}
