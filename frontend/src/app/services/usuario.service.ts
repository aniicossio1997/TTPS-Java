import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioUpdateRequest } from '../interfaces/usuarioUpdateRequest.interface';
import { UsuarioDetalleDTO } from '../interfaces/UsuarioDetalleDTO.interface';
import { RestablecerPasswordRequest } from '../interfaces/restablecerPasswordRequest.interface';
import { ApiResponse } from '../interfaces/local/ApiResponse.interface';
import { UsuarioSmall } from '../interfaces/usuario.interface.';


@Injectable({
  providedIn: 'root',
})
export class UsuarioService {

  private http = inject(HttpClient);

  private readonly apiUrl = `${environment.API_URL}/usuarios`;


  getUsuarioById(id: number): Observable<UsuarioDetalleDTO> {
    return this.http.get<UsuarioDetalleDTO>(`${this.apiUrl}/${id}`);
  }

  /**
   * Actualiza un usuario enviando multipart/form-data:
   *  - 'data': JSON con los datos del usuario (como en el curl)
   *  - 'file': imagen opcional
   */
  putUpdateUsuario(id: number,data: UsuarioUpdateRequest,file?: File | null): Observable<UsuarioSmall> {

    const formData = new FormData();
    // Parte JSON (equivalente a -F 'data={...};type=application/json')
    formData.append(
      'data',
      new Blob([JSON.stringify(data)], { type: 'application/json' })
    );

    // Parte file (opcional)
    if (file) {
      formData.append('file', file, file.name);
    }

    return this.http.put<UsuarioSmall>(`${this.apiUrl}/${id}`, formData);
  }

  ///api/usuarios/1/password
  cambiarPassword(userId: number, body: RestablecerPasswordRequest): Observable<ApiResponse> {

    return this.http.put<ApiResponse>(`${this.apiUrl}/${userId}/password`, body);
  }

  getUsuarios(): Observable<UsuarioSmall[]> {
    return this.http.get<UsuarioSmall[]>(`${this.apiUrl}`);
  }
}
