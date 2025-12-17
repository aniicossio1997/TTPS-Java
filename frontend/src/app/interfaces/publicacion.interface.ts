import { FotoLinkDTO } from './fotoLinkDTO';
import { PaginationFilter } from './pagination.interface';
import { Ubicacion, UbicacionCreate, UbicacionUpdate } from './ubicacion.interface';
import { ubicacionCreateRequest } from './ubicacionCreateRequest.interface';
import { UsuarioSmall } from './usuario.interface.';

export type EstadoPublicacionEnum = 'PERDIDO_PROPIO' | 'PERDIDO_AJENO' | 'RECUPERADO' | 'ADOPTADO';
export type Tamanio = 'PEQUENO' | 'MEDIANO' | 'GRANDE';
export type Color = 'Negro' | 'Blanco' | 'Marrón' | 'Gris' | 'Amarillo' |'Otro';
export type Especie = 'Perro' | 'Gato' | 'Ave' | 'Otro';

export interface EstadoPublicacion {
  estado: EstadoPublicacionEnum;
  fecha: Date;
}

export interface EstadoPublicacionCreate {
  estado: EstadoPublicacionEnum;
}

export interface Publicacion {
  id: number;
  nombre: string;
  descripcion: string;
  fecha: Date;
  color: Color;
  especie: Especie;
  tamanio: Tamanio;
  ubicacion: Ubicacion;
  usuarioId: number;
  estado?: EstadoPublicacion;
  usuario: UsuarioSmall;
  fotos: FotoLinkDTO[];
}

export interface PublicacionCreate {
  nombre: string;
  descripcion?: string;
  color: string;
  especie: string;
  tamanio: string;
  ubicacion: ubicacionCreateRequest;
  estado: EstadoPublicacionEnum;
  fecha?: Date;
}

export interface PublicacionUpdate extends Partial<Omit<PublicacionCreate, 'ubicacion'>> {
  ubicacion?: UbicacionUpdate;
}

export interface PublicacionFilter extends PaginationFilter<Publicacion> {
  nombre?: string;
  especie?: string;
  tamanio?: string;
  color?: string;
  fechaDesde?: Date;
  fechaHasta?: Date;
  usuarioId?: number;
  // Ubicacion
  departamento?: string;
  provincia?: string;
}
