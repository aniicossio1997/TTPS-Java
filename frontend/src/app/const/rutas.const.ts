export type AppRouteKey =
  | '/app/publicaciones'
  | '/app/publicaciones/crear'
  | '/app/publicaciones/detalle/:id'
  | '/app/publicaciones/editar/:id'
  | '/app/mapa'
  | '/app/perfil';

export const RUTA_CONST: Record<AppRouteKey, { titulo: string; parent?: AppRouteKey }> = {
  '/app/publicaciones': { titulo: 'Publicaciones' },

  '/app/publicaciones/crear': {
    titulo: 'Crear publicación',
    parent: '/app/publicaciones',
  },

  '/app/publicaciones/detalle/:id': {
    titulo: 'Detalle',
    parent: '/app/publicaciones',
  },

  '/app/publicaciones/editar/:id': {
    titulo: 'Editar publicación',
    parent: '/app/publicaciones',
  },

  '/app/mapa': {
    titulo: 'Mapa',
    parent: '/app/publicaciones',
  },

  '/app/perfil': { titulo: 'Perfil' },
};

export interface IRouteModel{
  title:string,
  routerLink:string[],
  icon:string,
}
export enum ManagementRoutes {
  Public='public',
  Usuario_comun = 'app',
  Admin = 'admin',

  Usuarios = 'usuarios',
  Publicaciones = 'publicaciones',
  Detail = 'detail',
  Perfil = 'perfil',


  Create = 'create',
  Edit = 'edit',

  Auth='auth',
  Login='login'

}
