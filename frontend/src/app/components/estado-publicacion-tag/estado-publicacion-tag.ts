import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Tag, TagModule } from 'primeng/tag';

interface EstadoTag {
  text: string;
  severity: Tag['severity'];
}

enum EstadoPublicacionEnum {
  PERDIDO_PROPIO = 'PERDIDO_PROPIO',
  PERDIDO_AJENO = 'PERDIDO_AJENO',
  RECUPERADO = 'RECUPERADO',
  ADOPTADO = 'ADOPTADO',
}

@Component({
  selector: 'app-estado-publicacion-tag',
  standalone: true,
  template: `
    <p-tag [severity]="tagData().severity" [rounded]="true" [value]="tagData().text" />
  `,
  imports: [CommonModule, TagModule],
})
export class EstadoPublicacionTag {
  estado = input<EstadoPublicacionEnum | string>('');

  tagData = (): EstadoTag => {
    const estadoEnum = this.estado();

    switch (estadoEnum) {
      case EstadoPublicacionEnum.PERDIDO_PROPIO:
        return { text: 'BUSCANDO A MI MASCOTA', severity: 'warn' };
      case EstadoPublicacionEnum.PERDIDO_AJENO:
        return { text: 'ENCONTRÉ UNA MASCOTA', severity: 'info' };
      case EstadoPublicacionEnum.RECUPERADO:
        return { text: 'RECUPERADO', severity: 'success' };
      case EstadoPublicacionEnum.ADOPTADO:
        return { text: 'ADOPTADO', severity: 'contrast' };
      default:
        return { text: 'ESTADO DESCONOCIDO', severity: 'secondary' };
    }
  };
}
