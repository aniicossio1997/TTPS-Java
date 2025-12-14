import { FotoLinkDTO } from "./fotoLinkDTO";
import { Ubicacion } from "./ubicacion.interface";

export interface  UsuarioDetalleDTO {
    id:       number;
    nombre:   string;
    apellido: string;
    email:    string;
    rol:      string;
    telefono: string;
    ubicacion: Ubicacion
    fotoLink: FotoLinkDTO
}
