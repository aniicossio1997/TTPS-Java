import { Component, computed, inject, signal, effect, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { switchMap, map, of, BehaviorSubject, Observable, catchError, startWith } from 'rxjs';

import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';

import { Publicacion, PublicacionFilter } from '../../../interfaces/publicacion.interface';
import { MapaPublicaciones } from '../../../components/mapa-publicaciones/mapa-publicaciones';
import { PublicacionesService } from '../../../services/publicaciones.service';
import { PerfilByUserStoreService } from '../../../store/perfilByUser.stored.service';
import { AuthStoreService } from '../../../store/auth.stored.service';

interface MapaState {
  loading: boolean;
  publicaciones: Publicacion[] | null;
  error: string | null;
}

@Component({
  selector: 'app-mapa-geografico-publicaciones',
  standalone: true,
  providers: [PerfilByUserStoreService], // Si el store debe ser de instancia
  imports: [
    CommonModule,
    CardModule,
    SkeletonModule,
    MessageModule,
    InputTextModule,
    MapaPublicaciones,
    FormsModule,
  ],
  templateUrl: './mapa-geografico-publicaciones.html',
})
export class MapaGeograficoPublicaciones implements OnInit {
  readonly perfilStore = inject(PerfilByUserStoreService);
  readonly authStore = inject(AuthStoreService);
  private publicacionesService = inject(PublicacionesService);

  // Se inicializa a nulo/undefined, esperando la carga del perfil
  departamentoInicial = computed(() => this.perfilStore.perfilDeUsuario()?.ubicacion?.departamento);
  ubicacionInicial = computed(() => {
    const ubicacion = this.perfilStore.perfilDeUsuario()?.ubicacion;
    if (!ubicacion) return '';
    return `(${ubicacion.departamento}, ${ubicacion.provincia})`;
  });

  public departamentoBusqueda = signal<string | undefined>(undefined);

  private filtroTrigger$ = new BehaviorSubject<PublicacionFilter>({
    departamento: undefined,
    page: 1,
    size: 100,
  });

  public mapaState$: Observable<MapaState>;
  private initialLoadDone = false;

  constructor() {
    this.mapaState$ = this.setupDataSubscription();

    // El effect se ejecuta la primera vez que departamentoInicial tiene un valor (cargado o vacío)
    effect(() => {
      const depto = this.departamentoInicial();

      // La clave es que el perfil ya resolvió (no es null) Y no hemos hecho una búsqueda manual
      if (this.perfilStore.perfilDeUsuario() !== null && !this.initialLoadDone) {
        // Si hay departamento, disparamos la búsqueda
        if (depto) {
          this.departamentoBusqueda.set(depto);
          this.filtroTrigger$.next({ departamento: depto, page: 1, size: 100 });
        } else {
          // Si el perfil cargó pero no tiene depto, disparamos el filtro vacío
          this.filtroTrigger$.next({ departamento: undefined, page: 1, size: 100 });
        }

        this.initialLoadDone = true; // Bloquea este effect para futuras ejecuciones (salvo cambios manuales)
      }
    });
  }

  ngOnInit(): void {
    const userId = this.authStore.usuario()?.id;
    if (userId) {
      // Asumo que _getPerfil maneja el loading y la actualización de la signal perfilDeUsuario()
      this.perfilStore._getPerfil(userId);
    }
  }

  private setupDataSubscription(): Observable<MapaState> {
    return this.filtroTrigger$.pipe(
      switchMap((filter) => {
        // Si la señal de perfil no ha resuelto (aún es null) o no hay departamento para buscar
        if (this.perfilStore.perfilDeUsuario() === null || !filter.departamento) {
          // Si el perfil aún está cargando, mostramos un estado de carga (el HTML lo maneja)
          if (this.perfilStore.perfilDeUsuario() === null) {
            return of({ loading: true, publicaciones: null, error: null });
          }

          // Si ya cargó pero no hay departamento (filtro vacío), mostramos el mensaje de error/prompt
          return of({
            loading: false,
            publicaciones: null,
            error: 'Ingrese un departamento para visualizar las publicaciones en el mapa.',
          });
        }

        return this.publicacionesService.getFiltered(filter).pipe(
          map((paginatedResult) => ({
            loading: false,
            // 🚩 Usamos 'paginatedResult.elements' como lo tenías en tu código
            publicaciones: paginatedResult.elements,
            error: null,
          })),
          catchError((err) =>
            of({
              loading: false,
              publicaciones: null,
              error: 'Error al cargar publicaciones. Intente más tarde.',
            })
          ),
          startWith({ loading: true, publicaciones: null, error: null })
        );
      })
    );
  }

  public buscarPorDepartamento(departamento: string): void {
    if (departamento?.trim()) {
      this.departamentoBusqueda.set(departamento);
      this.initialLoadDone = true;
      this.filtroTrigger$.next({
        departamento: departamento,
        page: 1,
        size: 100,
      });
    } else {
      this.departamentoBusqueda.set(undefined);
      this.filtroTrigger$.next({ departamento: undefined, page: 1, size: 100 });
    }
  }
}
