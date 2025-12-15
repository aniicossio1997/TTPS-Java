import { FotoLinkDTO } from "./fotoLinkDTO";
import { Ubicacion, UbicacionCreate } from "./ubicacion.interface";
import { UsuarioSmall } from "./usuario.interface.";

export interface Avistamiento {
  id: number;
  descripcion: string;
  agradecimiento: boolean;
  fecha: Date;
  ubicacion: Ubicacion;
  publicacionId: number;
  usuario: UsuarioSmall;
  fotos?: FotoLinkDTO[];
}

export interface AvistamientoCreate {
  descripcion: string;
  ubicacion: UbicacionCreate;
  publicacionId: number;
}

export interface AvistamientoFilter {
  publicacionId?: number;
  usuarioId?: number;
}