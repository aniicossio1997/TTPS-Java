import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import {
  Publicacion,
  PublicacionFilter,
} from '../interfaces/publicacion.interface';
import { PaginatedResult } from '../interfaces/pagination.interface';

@Injectable({
  providedIn: 'root',
})
export class PublicService extends ApiService {
  protected override readonly basePath = '/public';

  public getFiltered(filter: PublicacionFilter): Observable<PaginatedResult<Publicacion>> {
    const params = this.buildParams(filter);

    return this.get<PaginatedResult<Publicacion>>('/publicaciones', { params });
  }
}
