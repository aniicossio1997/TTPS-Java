export interface UbicacionExternaResponse {
    parametros: Parametros;
    ubicacion?:  Ubicacion;
}

export interface Parametros {
    lat: number;
    lon: number;
}

export interface Ubicacion {
    departamento?: Departamento;
    lat?:          number;
    lon?:          number;
    municipio?:    Departamento;
    provincia?:    Departamento;
}

export interface Departamento {
    id:     string;
    nombre: string;
}

export interface UbicacionLatitudLongitudResponse extends Parametros{

}
