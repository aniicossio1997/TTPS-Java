import { UsuarioSmall } from "./usuario.interface.";

export interface LoginResponse {
  token: string;
  usuario: UsuarioSmall;
}
