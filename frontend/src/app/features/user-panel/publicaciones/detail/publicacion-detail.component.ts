import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';


import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';
import { Tag, TagModule } from 'primeng/tag';
import { CarouselModule } from 'primeng/carousel';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { SplitButtonModule } from 'primeng/splitbutton';
import { PublicacionesService } from '../../../../services/publicaciones.service';
import { Publicacion } from '../../../../interfaces/publicacion.interface';
import { AuthStoreService } from '../../../../store/user.stored.service';

interface EstadoTag {
  text: string;
  severity: Tag['severity'];
}

@Component({
  selector: 'app-publicacion-detail',
  standalone: true,
  templateUrl: './publicacion-detail.component.html',
  imports: [
    CommonModule,
    RouterModule,
    DatePipe,
    CardModule,
    ButtonModule,
    SkeletonModule,
    MessageModule,
    TagModule,
    CarouselModule,
    SplitButtonModule,
    TieredMenuModule,
  ],
})
export class PublicacionDetailComponent implements OnInit {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authStore = inject(AuthStoreService);

  private publicacionesService = inject(PublicacionesService);

  estadoTag = computed<EstadoTag>(() => {
    const estadoEnum = this.publicacion()?.estado?.estado ?? '';

    switch (estadoEnum) {
      case 'PERDIDO_PROPIO':
        return { text: 'BUSCANDO A MI MASCOTA', severity: 'warn' };

      case 'PERDIDO_AJENO':
        return { text: 'ENCONTRÉ UNA MASCOTA', severity: 'info' };

      default:
        return { text: 'ESTADO DESCONOCIDO', severity: 'secondary' };
    }
  });

  isOwner = computed(() => {
    return this.authStore.usuario()?.id == this.publicacion()?.usuarioId;
  });


  private publicacionId$ = this.route.paramMap.pipe(
    switchMap((params) => {
      const id = params.get('id');
      if (id && !isNaN(+id)) {
        return this.publicacionesService.getById(+id);
      }
      this.router.navigate(['/404']);
      return [null];
    })
  );


  public publicacion = toSignal<Publicacion | null>(this.publicacionId$);

  public fotos = computed<string[]>(() => {
    if (!this.publicacion()) return [];
    if (this.publicacion()?.fotos?.length) return this.publicacion()?.fotos ?? [];
    return [
      'https://images.unsplash.com/photo-1517849845537-4d257902454a?q=80&w=800&auto=format&fit=crop',
    ];
  });


  public isLoading = signal(true);
  public error = signal<string | undefined>(undefined);

  options = [
    {
      label: 'Editar Publicación',
      icon: 'pi pi-pencil',
      command: () => this.editarPublicacion(this.publicacion()!.id),
    },
    { label: 'Marcar como Recuperada', icon: 'pi pi-check-circle', severity: 'success' },
    { label: 'Eliminar Publicación', icon: 'pi pi-trash', severity: 'danger' },
  ];

  public responsiveOptions = [
    { breakpoint: '1400px', numVisible: 1, numScroll: 1 },
    { breakpoint: '1199px', numVisible: 1, numScroll: 1 },
    { breakpoint: '767px', numVisible: 1, numScroll: 1 },
    { breakpoint: '575px', numVisible: 1, numScroll: 1 },
  ];

  ngOnInit(): void {
    this.publicacionId$.subscribe({
      next: () => {
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('No se encontró la publicación.');
        this.isLoading.set(false);
        console.error(err);
      },
    });
  }

  public editarPublicacion(id: number): void {

    this.router.navigate(['/app/publicaciones', 'editar', id]);
  }

  public verUbicacion(ubicacion: any): void {

    const url = `https://www.google.com/maps/search/?api=1&query=${ubicacion.latitud},${ubicacion.longitud}`;
    window.open(url, '_blank');
  }
}
