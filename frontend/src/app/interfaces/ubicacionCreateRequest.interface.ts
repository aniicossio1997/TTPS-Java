// src/app/core/models/auth/register-request.model.ts
export interface ubicacionCreateRequest {
  latitud: number;
  longitud: number;

  provincia: string;
  idExternoProvincia: string;

  municipio: string;
  idExternoMunicipio: string;

  departamento: string; // en Java estaba con mayúscula Departamento
  idExternoDepartamento: string;
}
