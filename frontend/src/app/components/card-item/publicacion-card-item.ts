import { EstadoPublicacionEnum } from './../../interfaces/publicacion.interface';
import { Component, Input, computed, inject, input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { Tag, TagModule  } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { Publicacion } from '../../interfaces/publicacion.interface';
import { AuthStoreService } from '../../store/auth.stored.service';
import { EstadoPublicacionTag } from '../estado-publicacion-tag/estado-publicacion-tag';
import { HighlightSearchPipe } from '../../shared/pipes/highlight-search.pipe';
import { UserAvatarComponent } from '../user-avatar/user-avatar';

interface EstadoTag {
  text: string;
  severity: Tag['severity'];
}

@Component({
  selector: 'app-publicacion-card-item',
  standalone: true,
  templateUrl: './publicacion-card-item.html',
  imports: [CommonModule, CardModule, TagModule, ButtonModule, DatePipe, EstadoPublicacionTag, HighlightSearchPipe, UserAvatarComponent ],
})
export class PublicacionCardItem {
  private router = inject(Router);
  public authStore = inject(AuthStoreService);

  searchText = input<string | null | undefined>(null)

  readOnly = input<boolean>(true)

  @Input({ required: true }) publicacion!: Publicacion;

  estadoTag = computed<EstadoTag>(() => {
    const estadoEnum = this.publicacion?.estado?.estado ?? '';

    switch (estadoEnum) {
      case 'PERDIDO_PROPIO':
        return { text: 'BUSCANDO A MI MASCOTA', severity: 'warn' };

      case 'PERDIDO_AJENO':
        return { text: 'ENCONTRÉ UNA MASCOTA', severity: 'info' };

      case 'RECUPERADO':
        return { text: 'RECUPERADO', severity: 'success' };

      default:
        return { text: 'ESTADO DESCONOCIDO', severity: 'secondary' };
    }
  });

  puedoEdit = computed(()=>{
    return(
    this.authStore.usuario() && this.authStore.usuario()?.id ==this.publicacion.usuarioId
    && (this.publicacion.estado?.estado== 'PERDIDO_AJENO' ||this.publicacion.estado?.estado== 'PERDIDO_PROPIO' )
    && this.readOnly())

    })

  defaultImageUrl = 'https://placedog.net/500';

  navigateToDetail(id: number): void {
    if (!this.authStore.usuario()) return;

    if (this.authStore.isAuthenticated() && this.authStore.isAdmin()) {
       this.router.navigate(['/admin/publicaciones/detalle', id]);
       return
    }
    if(this.authStore.isAuthenticated() && !this.authStore.isAdmin()){
      this.router.navigate([ '/app/publicaciones/detalle', id]);
      return;
    }
    this.router.navigate(['/public/login']);

  }

  verUbicacion(ubicacion: any): void {
    // Aquí puedes implementar la lógica para abrir un modal de mapa o redirigir

  }

  navigateToEdit(id: number){
    if (!this.authStore.usuario()) return;

    if (this.authStore.isAuthenticated() && this.authStore.isAdmin()) {
       this.router.navigate(['/admin/publicaciones/editar', id]);
       return
    }
    if(this.authStore.isAuthenticated() && !this.authStore.isAdmin()){
      this.router.navigate([ '/app/publicaciones/editar', id]);
      return;
    }
    this.router.navigate(['/public/login']);
  }

  get destinationDetailUrl(): string {
    if (this.authStore.isAuthenticated() && this.authStore.isAdmin()) {
      return '/admin/publicaciones/crear';
    }
    if(this.authStore.isAuthenticated() && !this.authStore.isAdmin()){
      return '/app/publicaciones/crear';
    }
    return '/public/login';
  }




  get estadoPublicacion(){
    return EstadoPublicacionTag;
  }
}
