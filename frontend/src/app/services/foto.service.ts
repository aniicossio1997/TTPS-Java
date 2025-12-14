import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { FotoLinkDTO } from '../interfaces/fotoLinkDTO';

@Injectable({
  providedIn: 'root',
})
export class FotoService {

  private http = inject(HttpClient);

  private readonly apiUrl = `${environment.API_URL}/fotos`;

  getFotoUsuario(usuarioId: number) {
    return this.http.get<FotoLinkDTO>(`${this.apiUrl}/usuario/${usuarioId}`);
  }


}
