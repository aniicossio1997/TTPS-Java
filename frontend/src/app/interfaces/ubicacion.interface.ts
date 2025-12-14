export interface Ubicacion {
  id: number;
  latitud: number;
  longitud: number;

  provincia: string;
  idExternoProvincia: string;

  municipio: string;
  idExternoMunicipio: string;

  departamento: string; // en Java estaba con mayúscula Departamento
  idExternoDepartamento: string;
}

export interface UbicacionCreate extends Omit<Ubicacion, 'id'> {}

export interface UbicacionUpdate extends Partial<Ubicacion> {}
