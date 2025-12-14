export interface Ubicacion {
  id: number;
  idExterno: string;
  provincia: string;
  ciudad: string;
  barrio: string;
  latitud: number;
  longitud: number;

  idExternoProvincia: string;

  municipio: string;
  idExternoMunicipio: string;

  departamento: string; // en Java estaba con mayúscula Departamento
  idExternoDepartamento: string;
}

export interface UbicacionCreate extends Omit<Ubicacion, 'id'> {}

export interface UbicacionUpdate extends Partial<Ubicacion> {}
