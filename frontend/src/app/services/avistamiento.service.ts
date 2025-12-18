import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import {
  Avistamiento,
  AvistamientoCreate,
  AvistamientoFilter,
} from '../interfaces/avistamiento.interface';

@Injectable({
  providedIn: 'root',
})
export class AvistamientoService extends ApiService {
  private readonly endpoint = '/avistamientos';

  public getFiltered(filter: AvistamientoFilter): Observable<Avistamiento[]> {
    const params = this.buildParams(filter);


    return this.get<Avistamiento[]>(this.endpoint, { params });
  }

  public getById(id: number): Observable<Avistamiento> {
    return this.get<Avistamiento>(`${this.endpoint}/${id}`);
  }

  public create(avistamiento: AvistamientoCreate, imagenes?: File[]): Observable<Avistamiento> {
    const formData = this.buildFormData(avistamiento, imagenes);
    return this.post<Avistamiento, FormData>(this.endpoint, formData);
  }

  public delete(id: number): Observable<void> {
    return this._delete<void>(`${this.endpoint}/${id}`);
  }
}
