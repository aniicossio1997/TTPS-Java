import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { Medalla, MedallaEnum } from '../../../../interfaces/medalla.interface';

@Component({
  selector: 'app-medallas-section',
  standalone: true,
  imports: [CommonModule, CardModule, TagModule],
  templateUrl: './medallas-section.html',
  styleUrl: './medallas-section.scss',
})
export class MedallasSection {
  @Input() medallas: Medalla[] = [];

 private readonly MEDALLA_MAP: Record<string, { nombre: string; emoji: string; color: string; descripcion: string }> = {
  [MedallaEnum.RESCATISTA_NIVEL_1]: {
    nombre: 'Rescatista Nivel 1',
    emoji: '🥉',
    color: 'bg-orange-100 text-orange-700',
    descripcion:  'Has participando en una búsqueda.'
  },
  [MedallaEnum.RESCATISTA_NIVEL_2]: {
    nombre: 'Rescatista Nivel 2',
    emoji: '🥈',
    color: 'bg-slate-200 text-slate-700',
    descripcion: 'Has demostrado compromiso participando en múltiples búsquedas.'
  },
  [MedallaEnum.RESCATISTA_NIVEL_3]: {
    nombre: 'Rescatista Nivel 3',
    emoji: '🥇',
    color: 'bg-yellow-100 text-yellow-700',
    descripcion: 'Experto en salvamento con una trayectoria admirable.'
  },
  [MedallaEnum.HEROE_DEL_BARRIO]: {
    nombre: 'Héroe del Barrio',
    emoji: '🏘️',
    color: 'bg-blue-100 text-blue-700',
    descripcion: 'Reconocimiento por tu ayuda constante a la comunidad local.'
  },
  [MedallaEnum.ANGEL_GUARDIAN]: {
    nombre: 'Ángel Guardián',
    emoji: '👼',
    color: 'bg-cyan-100 text-cyan-700',
    descripcion: 'Por velar desinteresadamente por el bienestar de los más vulnerables.'
  },
  [MedallaEnum.NUEVO_TUTOR]: {
    nombre: 'Nuevo Tutor',
    emoji: '💗',
    color: 'bg-red-50 text-red-700', // Ajusté el color a rojo para combinar con el corazón
    descripcion: '¡Bienvenido! Has asumido la responsabilidad de cuidar una vida.'
  },
};

  getConfig(tipo: MedallaEnum) {
    return (
      this.MEDALLA_MAP[tipo] || { nombre: tipo, emoji: '🏅', color: 'bg-gray-100 text-gray-700', descripcion: '' }
    );
  }
}
