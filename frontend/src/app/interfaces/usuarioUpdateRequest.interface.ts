import { ubicacionCreateRequest } from "./ubicacionCreateRequest.interface";


export interface UsuarioUpdateRequest {
  nombre: string;
  apellido: string;
  email: string;
  telefono?: string;
  ubicacion: ubicacionCreateRequest;
}
