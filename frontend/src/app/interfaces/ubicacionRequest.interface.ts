// src/app/core/models/auth/register-request.model.ts
export interface UbicacionRequest {
  idExterno: string;
  provincia: string;
  ciudad: string;
  barrio: string;
  latitud: number;
  longitud: number;
}
