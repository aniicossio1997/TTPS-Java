import { FotoLinkDTO } from "./fotoLinkDTO";
import { Ubicacion, UbicacionCreate } from "./ubicacion.interface";
import { ubicacionCreateRequest } from "./ubicacionCreateRequest.interface";
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
  ubicacion: ubicacionCreateRequest;
  publicacionId: number;
}

export interface AvistamientoFilter {
  publicacionId?: number;
  usuarioId?: number;
}