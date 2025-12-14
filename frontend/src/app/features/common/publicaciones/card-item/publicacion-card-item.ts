import { Component, Input, computed, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { Tag, TagModule, TagPassThroughOptions } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { Publicacion } from '../../../../interfaces/publicacion.interface';
import { AuthService } from '../../../../services/auth.service';
import { AuthStoreService } from '../../../../store/auth.stored.service';

interface EstadoTag {
  text: string;
  severity: Tag['severity'];
}

@Component({
  selector: 'app-publicacion-card-item',
  standalone: true,
  templateUrl: './publicacion-card-item.html',
  imports: [CommonModule, CardModule, TagModule, ButtonModule, DatePipe],
})
export class PublicacionCardItem {
  private router = inject(Router);
  private authStore = inject(AuthStoreService);

  @Input({ required: true }) publicacion!: Publicacion;

  estadoTag = computed<EstadoTag>(() => {
    const estadoEnum = this.publicacion?.estado?.estado ?? '';

    switch (estadoEnum) {
      case 'PERDIDO_PROPIO':
        return { text: 'BUSCANDO A MI MASCOTA', severity: 'warn' };

      case 'PERDIDO_AJENO':
        return { text: 'ENCONTRÉ UNA MASCOTA', severity: 'info' };

      default:
        return { text: 'ESTADO DESCONOCIDO', severity: 'secondary' };
    }
  });

  defaultImageUrl = 'https://placedog.net/500';

  navigateToDetail(id: number): void {
    if (!this.authStore.usuario()) return;
    this.router.navigate(['/app/publicaciones/detalle/', id]);
  }

  verUbicacion(ubicacion: any): void {
    // Aquí puedes implementar la lógica para abrir un modal de mapa o redirigir
    console.log('Ver ubicación:', ubicacion);
  }
}
