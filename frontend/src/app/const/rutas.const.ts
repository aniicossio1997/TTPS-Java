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

