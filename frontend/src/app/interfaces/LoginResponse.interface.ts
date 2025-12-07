export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: string;
}

export interface LoginResponse {
  token: string;
  usuario: Usuario;
}
