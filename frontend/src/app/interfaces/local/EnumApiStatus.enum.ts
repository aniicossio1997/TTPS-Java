export enum EnumApiStatus {
  INIT = 'init',
  LOADING = 'loading',
  SUCCESS = 'finalize',
  ERROR = 'error'
}

/**
 * Estados posibles para operaciones asíncronas de API
 */
export const ApiStatus = {
  /** Estado inicial antes de cualquier operación */
  INIT: 'init',
  /** Operación en progreso */
  LOADING: 'loading',
  /** Operación completada con éxito */
  SUCCESS: 'success',
  /** Operación fallida */
  ERROR: 'error',
  /** Operación cancelada manualmente */
  CANCELLED: 'cancelled',

  NOT_FOUND: 'not_found'
} as const;

export type ApiStatus = typeof ApiStatus[keyof typeof ApiStatus];
