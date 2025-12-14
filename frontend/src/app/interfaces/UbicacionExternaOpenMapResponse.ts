export interface UbicacionExternaOpenMapResponse {
    lat:              number;
    lng:              number;
    ubicacionExterna?: UbicacionExterna;
}

export interface UbicacionExterna {
    ubicacion:  Ubicacion;
    parametros: Parametros;
}

export interface Parametros {
    lat: number;
    lon: number;
}

export interface Ubicacion {
    lat:          number;
    lon:          number;
    departamento: Departamento;
    municipio:    Departamento;
    provincia:    Departamento;
}

export interface Departamento {
    id:     string;
    nombre: string;
}
