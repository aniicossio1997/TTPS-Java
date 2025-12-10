import { PaginationFilter } from './pagination.interface';
import { Ubicacion, UbicacionCreate, UbicacionUpdate } from './ubicacion.interface';

export type EstadoPublicacionEnum = 'PERDIDO_PROPIO' | 'PERDIDO_AJENO' | 'RECUPERADO' | 'ADOPTADO';

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
  color: string;
  especie: string;
  raza: string;
  tamanio: string;
  ubicacion: Ubicacion;
  usuarioId: number;
  estado?: EstadoPublicacion;
  fotos: string[];
}

export interface PublicacionCreate {
  nombre: string;
  descripcion?: string;
  color: string;
  especie: string;
  raza: string;
  tamanio: string;
  ubicacion: UbicacionCreate;
  estado: EstadoPublicacionEnum;
}

export interface PublicacionUpdate extends Partial<Omit<PublicacionCreate, 'ubicacion'>> {
  ubicacion: UbicacionUpdate;
}

export interface PublicacionFilter extends PaginationFilter<Publicacion> {
  nombre?: string;
  especie?: string;
  raza?: string;
  tamanio?: string;
  color?: string;
  fechaDesde?: Date;
  fechaHasta?: Date;
  usuarioId?: number;
}
