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

    console.log("buscando avistamientos", params)
    return this.get<Avistamiento[]>(this.endpoint, { params });
  }

  public getById(id: number): Observable<Avistamiento> {
    return this.get<Avistamiento>(`${this.endpoint}/${id}`);
  }

  public create(avistamiento: AvistamientoCreate): Observable<Avistamiento> {
    return this.post<Avistamiento, AvistamientoCreate>(this.endpoint, avistamiento);
  }

  public delete(id: number): Observable<void> {
    return this._delete<void>(`${this.endpoint}/${id}`);
  }
}
