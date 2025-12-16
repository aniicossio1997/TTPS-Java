import { Component, inject, signal, OnInit, computed, ViewChild } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';

import { CardModule } from 'primeng/card';
import { Button, ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';
import { Tag, TagModule } from 'primeng/tag';
import { CarouselModule } from 'primeng/carousel';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SplitButtonModule } from 'primeng/splitbutton';
import { PublicacionesService } from '../../../../services/publicaciones.service';
import { AuthStoreService } from '../../../../store/auth.stored.service';
import { EstadoPublicacionEnum, Publicacion } from '../../../../interfaces/publicacion.interface';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Avistamiento } from '../../../../interfaces/avistamiento.interface';
import { AvistamientoForm } from '../../avistamientos/form/avistamiento-form';
import { AvistamientosList } from '../../avistamientos/list/avistamientos-list';
import { ToastrService } from 'ngx-toastr';
import { EstadoPublicacionTag } from '../../../../components/estado-publicacion-tag/estado-publicacion-tag';
import { DividerModule } from 'primeng/divider';
import { MapaPublicaciones } from '../../../../components/mapa-publicaciones/mapa-publicaciones';
import { UserAvatarComponent } from '../../../../components/user-avatar/user-avatar';

interface EstadoTag {
  text: string;
  severity: Tag['severity'];
}

export interface ActionOption {
  label: string;
  icon: string;
  command?: () => void;
  severity?: Button['severity'];
  show?: boolean;
}

@Component({
  selector: 'app-publicacion-detail',
  standalone: true,
  templateUrl: './publicacion-detail.component.html',
  providers: [ConfirmationService, MessageService],
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
    ConfirmDialogModule,
    AvistamientoForm,
    AvistamientosList,
    EstadoPublicacionTag,
    DividerModule,
    MapaPublicaciones,
    UserAvatarComponent,
  ],
})
export class PublicacionDetailComponent implements OnInit {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authStore = inject(AuthStoreService);
  private confirmationService = inject(ConfirmationService);
  private publicacionesService = inject(PublicacionesService);
  private readonly toastr = inject(ToastrService);

  @ViewChild('avistamientosList') avistamientosList!: AvistamientosList;

  avistamientos: Avistamiento[] = [];
  displayAvistamientoModal: boolean = false;
  selectedAvistamiento: Avistamiento | null = null;

  COLOR_MAP: Record<string, string> = {
    negro: '#000000',
    blanco: '#FFFFFF',
    marron: '#8B4513',
    gris: '#808080',
    amarillo: '#edf113',
    otro: '#D3D3D3',
  };

  isOwner = computed(() => {
    return this.authStore.usuario()?.id == this.publicacion()?.usuarioId;
  });

  puedeEditar = computed(() => {
    const publicacion = this.publicacion();
    if (!publicacion || !publicacion.estado?.estado) return false;
    return !['RECUPERADO', 'ADOPTADO'].includes(publicacion.estado.estado);
  });

  estado = computed<EstadoPublicacionEnum | undefined>(() => {
    const publicacion = this.publicacion();
    return publicacion?.estado?.estado;
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
    return this.publicacion()?.fotos.map((f) => f.url) || [];
  });

  public publicacionArray = computed<Publicacion[]>(() => {
    const publicacion = this.publicacion();
    if (!publicacion) return [];
    return [publicacion];
  });

  public isLoading = signal(true);
  public error = signal<string | undefined>(undefined);

  options = computed<ActionOption[]>(() =>[
    {
      label: 'Editar Publicación',
      icon: 'pi pi-pencil',
      command: () => this.editarPublicacion(this.publicacion()!.id),
      severity: 'success',
      show: this.puedeEditar(),
    },
    {
      label: 'Marcar como Recuperada',
      icon: 'pi pi-check-circle',
      severity: 'secondary',
      command: () => this.confirmarRecuperada(),
      show: this.puedeEditar(),
    },
    {
      label: 'Adoptar',
      icon: 'pi pi-heart',
      severity: 'help',
      command: () => this.confirmarAdopcion(),
      show: this.estado() == 'PERDIDO_AJENO',
    },
    {
      label: 'Archivar Publicación',
      icon: 'pi pi-trash',
      severity: 'danger',
      command: () => this.confirmarArchivo(),
      show: true,
    },
  ]);

  public responsiveOptions = [
    { breakpoint: '1400px', numVisible: 1, numScroll: 1 },
    { breakpoint: '1199px', numVisible: 1, numScroll: 1 },
    { breakpoint: '767px', numVisible: 1, numScroll: 1 },
    { breakpoint: '575px', numVisible: 1, numScroll: 1 },
  ];

  onAvistamientosCargados(avistamientos: Avistamiento[]): void {
    console.log({ avistamientos });
    this.avistamientos = avistamientos;
  }

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

  public confirmarArchivo(): void {
    this.confirmationService.confirm({
      message:
        '¿Está seguro de que desea archivar esta publicación? Esta acción no se puede revertir.',
      header: 'Achivar publicación',
      icon: 'pi pi-info-circle',
      acceptLabel: 'Sí, archivar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',

      accept: () => {
        this.archivarPublicacion();
      },
    });
  }

  public archivarPublicacion(): void {
    const id = this.publicacion()?.id;
    if (!id) return;
    this.publicacionesService.delete(id).subscribe({
      next: () => {
        this.toastr.success('Se ha archivado la publicación.');
        this.router.navigate(['/app/publicaciones']);
      },
    });
  }

  public confirmarRecuperada(): void {
    const title =
      this.publicacion()?.estado?.estado == 'PERDIDO_PROPIO'
        ? '¿Has encontrado a tu mascota?'
        : '¿Has encontrado al dueño de la mascota?';
    this.confirmationService.confirm({
      message: '¡No sabes cuanto nos alegra! La publicación ya no podrá modificarse.',
      header: title,
      acceptLabel: 'Aceptar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.marcarRecuperada();
      },
    });
  }

  public confirmarAdopcion(): void {
    const title = '¿Quieres adoptar a esta mascota?';
    this.confirmationService.confirm({
      message: '¡No sabes cuanto nos alegra! La publicación ya no podrá modificarse.',
      header: title,
      acceptLabel: 'Aceptar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.adoptar();
      },
    });
  }

  private adoptar() {
    const id = this.publicacion()?.id;
    if (!id) return;

    this.publicacionesService.update(id, { estado: 'ADOPTADO' }).subscribe({
      next: () => {
        this.toastr.success('Felicidades! Tienes una nueva mascota.');
        this.router.navigate(['/app/publicaciones']);
      },
    });
  }

  private marcarRecuperada() {
    const id = this.publicacion()?.id;
    if (!id) return;

    this.publicacionesService.update(id, { estado: 'RECUPERADO' }).subscribe({
      next: () => {
        this.toastr.success('Se ha archivado la publicación.');
        this.router.navigate(['/app/publicaciones']);
      },
    });
  }
  // Avistamientos

  showAvistamientoModal(avistamiento?: Avistamiento): void {
    this.displayAvistamientoModal = true;
    if (avistamiento) {
      this.selectedAvistamiento = avistamiento;
    }
  }

  handleAvistamientoDisplayChange(open: boolean): void {
    this.displayAvistamientoModal = open;
    if (!open) {
      this.selectedAvistamiento = null;
    }
  }

  handleAvistamientoSuccess(): void {
    this.avistamientosList.recargar();
  }
}
