export interface Ubicacion {
  id: number;
  latitud: number;
  longitud: number;
  direccion: string;
}

export interface UbicacionCreate extends Omit<Ubicacion, 'id'> {}

export interface UbicacionUpdate extends Partial<Ubicacion> {}
