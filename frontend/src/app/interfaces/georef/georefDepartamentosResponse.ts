import { DepartamentoDTO } from "./departamentoDTO";

export interface GeorefDepartamentosResponse {
  departamentos: DepartamentoDTO[];
  cantidad: number;
  total: number;
  inicio: number;
}
