import { FotoLinkDTO } from "./fotoLinkDTO";
import { EstadoUsuarioEnum } from "./local/estadoUsuarioEnum";
import { Medalla } from "./medalla.interface";
import { Ubicacion } from "./ubicacion.interface";

export interface  UsuarioDetalleDTO {
    id:       number;
    nombre:   string;
    apellido: string;
    email:    string;
    rol:      string;
    telefono: string;
    ubicacion: Ubicacion;
    fotoLink?: FotoLinkDTO;
    medallas: Medalla[];
    estado: EstadoUsuarioEnum
    puntos: number;
}
