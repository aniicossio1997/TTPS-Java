export enum MedallaEnum {
  RESCATISTA_NIVEL_1 = 'RESCATISTA_NIVEL_1',
  RESCATISTA_NIVEL_2 = 'RESCATISTA_NIVEL_2',
  RESCATISTA_NIVEL_3 = 'RESCATISTA_NIVEL_3',
  HEROE_DEL_BARRIO = 'HEROE_DEL_BARRIO',
  ANGEL_GUARDIAN = 'ANGEL_GUARDIAN',
  NUEVO_TUTOR = 'NUEVO_TUTOR'
}

export interface Medalla {
    id: number;
    tipo: MedallaEnum;
    fechaAsignacion: Date;
}