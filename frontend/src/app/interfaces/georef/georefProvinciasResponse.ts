import { ProvinciaDTO } from './provinciaDTO';

export interface GeorefProvinciasResponse {
  provincias: ProvinciaDTO[];
  cantidad: number;
  total: number;
  inicio: number;
}
