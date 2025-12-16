import { Component, computed, effect, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { InputGroupModule } from 'primeng/inputgroup';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { PopoverModule } from 'primeng/popover';
import { SkeletonModule } from 'primeng/skeleton';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { TagModule } from 'primeng/tag';
import { MessageModule } from 'primeng/message';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { PublicService } from '../../../services/public.service';
import { Publicacion, PublicacionFilter } from '../../../interfaces/publicacion.interface';
import { PaginatedResult } from '../../../interfaces/pagination.interface';
import { RouterModule } from '@angular/router';
import { PublicacionCardItem } from '../../../components/card-item/publicacion-card-item';
import { AuthStoreService } from '../../../store/auth.stored.service';
import { UsuariosRanking } from "../../../components/usuarios-ranking/usuarios-ranking";

interface PublicacionState {
  data: Publicacion[];
  totalElements: number;
  pageSize: number;
  isLoading: boolean;
  error: string | null;
}

@Component({
  selector: 'app-publicaciones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    ButtonModule,
    InputTextModule,
    MessageModule,
    ProgressSpinnerModule,
    InputGroupModule,
    TagModule,
    SelectModule,
    DatePickerModule,
    PopoverModule,
    SkeletonModule,
    InputGroupAddonModule,
    PaginatorModule,
    RouterModule,
    PublicacionCardItem,
    UsuariosRanking
],
  templateUrl: './publicaciones.component.html',
})
export class PublicacionesComponent {
  private readonly publicService = inject(PublicService);
  private readonly authStore = inject(AuthStoreService);

  public readonly tamanioOptions = ['PEQUEÑO', 'MEDIANO', 'GRANDE', 'EXTRAGRANDE'];
  public readonly especieOptions = ['PERRO', 'GATO', 'AVE', 'OTRO'];
  private readonly defaultFiltro: PublicacionFilter = {
    page: 1,
    size: 10,
    sortBy: 'fecha',
    sortDir: 'desc',
  };

  public filtro = signal<PublicacionFilter>(this.defaultFiltro);

  public publicacionesState = signal<PublicacionState>({
    data: [],
    totalElements: 0,
    isLoading: true,
    error: null,
    pageSize: 10,
  });

  public totalPages = computed(() => {
    const total = this.publicacionesState().totalElements;
    const size = this.filtro().size;
    return Math.ceil(total / size);
  });

  public isLoading = computed(() => this.publicacionesState().isLoading);
  public error = computed(() => this.publicacionesState().error);
  public publicaciones = computed(() => this.publicacionesState().data);
  public pageSize = computed(() => this.publicacionesState().pageSize);
  public total = computed(() => this.publicacionesState().totalElements);

  public currentPage = computed(() => this.filtro().page);

  constructor() {
    effect(
      () => {
        const currentFiltro = this.filtro();

        this.publicacionesState.update((state) => ({
          ...state,
          isLoading: true,
          error: null,
        }));

        this.publicService
          .getFiltered(currentFiltro)
          .pipe(
            catchError((err: HttpErrorResponse) => {
              const errorMessage = `Error al cargar: ${err.statusText || 'Desconocido'}`;

              this.publicacionesState.update((state) => ({
                ...state,
                data: [],
                isLoading: false,
                error: errorMessage,
              }));
              return of(null as unknown as PaginatedResult<Publicacion>);
            })
          )
          .subscribe((response) => {
            if (response) {
              this.publicacionesState.set({
                data: response.elements,
                totalElements: response.totalElements,
                isLoading: false,
                error: null,
                pageSize: response.size,
              });
            }
          });
      },
      { allowSignalWrites: true }
    );
  }

  cambiarPagina(event: PaginatorState) {
    this.filtro.update((f) => ({ ...f, size: event.rows ?? 10, page: (event.page ?? 1) + 1 }));
  }

  public aplicarFiltros(): void {
    this.filtro.update((f) => ({ ...f, page: 1 }));
  }

  public resetFiltros(): void {
    this.filtro.set(this.defaultFiltro);
  }

  limpiarNombre() {
    const f = this.filtro();
    this.filtro.set({ ...f, nombre: '' });
  }

  public actualizarFiltro(campo: keyof PublicacionFilter, valor: any): void {
    this.filtro.update((f) => ({
      ...f,
      [campo]: valor,
      page: 1,
    }));
  }

  get destinationUrl(): string {
    if (this.authStore.isAuthenticated()) {
      return '/app/publicaciones/crear';
    }
    return '/login';
  }
}
