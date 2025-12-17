import { Component, computed, effect, inject, OnInit, signal } from '@angular/core';
import { PublicacionCardItem } from '../../../../components/card-item/publicacion-card-item';
import { catchError, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Publicacion, PublicacionFilter } from '../../../../interfaces/publicacion.interface';
import { PublicService } from '../../../../services/public.service';
import { AuthStoreService } from '../../../../store/auth.stored.service';
import { PaginatedResult } from '../../../../interfaces/pagination.interface';
import { ApiStatus } from '../../../../interfaces/local/EnumApiStatus.enum';
import { CommonModule } from '@angular/common';
import { Message } from 'primeng/message';


interface PublicacionState {
  data: Publicacion[];
  totalElements: number;
  pageSize: number;
  status: ApiStatus;
  error: string | null;
}

@Component({
  selector: 'app-pulicaciones-user',
  imports: [PublicacionCardItem,CommonModule,Message],
  templateUrl: './pulicaciones-user.html',
  styleUrl: './pulicaciones-user.scss',
})
export class PulicacionesUser implements OnInit {
  private readonly publicService = inject(PublicService);
  private readonly authStore = inject(AuthStoreService);


  public publicacionesState = signal<PublicacionState>({
    data: [],
    totalElements: 0,
    pageSize: 250,
    status: ApiStatus.INIT,
    error:null
  });


  public isLoading = computed(() => this.publicacionesState().status == ApiStatus.LOADING);
  public isError = computed(() => this.publicacionesState().status ==ApiStatus.ERROR);

  public isSuccess = computed(() => this.publicacionesState().status ==ApiStatus.SUCCESS);

  public isNotFound = computed(() => this.publicacionesState().status ==ApiStatus.NOT_FOUND);


  public publicaciones = computed(() => this.publicacionesState().data);
  public pageSize = computed(() => this.publicacionesState().pageSize);
  public total = computed(() => this.publicacionesState().totalElements);


  constructor() {

  }

  ngOnInit(): void {
    this._call_api()
  }


  get destinationUrl(): string {
    if (this.authStore.isAuthenticated()) {
      return '/app/publicaciones/crear';
    }
    return '/public/login';
  }

  private _call_api(){
    this.publicacionesState.set({...this.publicacionesState(), status:ApiStatus.LOADING})
    this.publicService
          .getFiltered({size:2500,page:1,usuarioId: this.authStore.usuario()?.id})
          .pipe(
            catchError((err: HttpErrorResponse) => {
              const errorMessage = `Error al cargar: ${err.statusText || 'Desconocido'}`;

              this.publicacionesState.update((state) => ({
                ...state,
                data: [],
               status:ApiStatus.ERROR,
                error: errorMessage,
              }));
              return of(null as unknown as PaginatedResult<Publicacion>);
            })
          )
          .subscribe((response) => {

            if (response.totalElements>0) {
              this.publicacionesState.set({
                data: response.elements,
                totalElements: response.totalElements,
                error: null,
                pageSize: response.size,
                status:ApiStatus.SUCCESS
              });

            }else{
                this.publicacionesState.update((state) => ({
                ...state,
                status:ApiStatus.NOT_FOUND,
              }));
            }
    });
  }
}
