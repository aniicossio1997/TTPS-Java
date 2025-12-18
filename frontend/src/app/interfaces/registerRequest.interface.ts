import { EnumRolUsuario } from "./local/rol-usuario.enum";
import { UbicacionCreate } from "./ubicacion.interface";

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  rol: EnumRolUsuario; // Ajustalo a tus roles reales
  ubicacion: UbicacionCreate;
  telefono: string;
}
