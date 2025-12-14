import { EnumRolUsuario } from './local/rol-usuario.enum';

export interface UsuarioSmall {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: EnumRolUsuario;

  //foto: string;
}
