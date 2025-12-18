import { FotoLinkDTO } from './fotoLinkDTO';
import { EstadoUsuarioEnum } from './local/estadoUsuarioEnum';
import { EnumRolUsuario } from './local/rol-usuario.enum';

export interface UsuarioSmall {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: EnumRolUsuario;
  puntos: number;
  foto?: FotoLinkDTO;

  estado: EstadoUsuarioEnum
  //foto: string;
}
