import { UbicacionCreate } from "./ubicacion.interface";


export interface UsuarioUpdateRequest {
  nombre: string;
  apellido: string;
  email: string;
  telefono?: string;
  ubicacion: UbicacionCreate;
}
