import { MunicipioDTO } from "./municipioDTO";

export interface GeorefMunicipiosResponse {
  municipios: MunicipioDTO[];
  cantidad: number;
  total: number;
  inicio: number;
}
