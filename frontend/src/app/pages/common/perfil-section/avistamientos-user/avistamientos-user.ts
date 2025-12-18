import { Component, computed, inject, input, signal } from '@angular/core';
import { Avistamiento } from '../../../../interfaces/avistamiento.interface';
import { ApiStatus } from '../../../../interfaces/local/EnumApiStatus.enum';
import { AvistamientoService } from '../../../../services/avistamiento.service';
import { AuthStoreService } from '../../../../store/auth.stored.service';
import { ToastrService } from 'ngx-toastr';
import { Message } from 'primeng/message';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { CarouselModule } from 'primeng/carousel';
import { SkeletonModule } from 'primeng/skeleton';
import { UserAvatarComponent } from '../../../../components/user-avatar/user-avatar';
import { CommonModule } from '@angular/common';


interface AvistamientoState {
  data: Avistamiento[];
  status: ApiStatus;
  error: string | null;
}

@Component({
  selector: 'app-avistamientos-user',
  imports: [
    Message,

    CardModule,
    TagModule,
    ButtonModule,
    DividerModule,
    CarouselModule,
    SkeletonModule,
    UserAvatarComponent,
    CommonModule
  ],
  templateUrl: './avistamientos-user.html',
  styleUrl: './avistamientos-user.scss',
})
export class AvistamientosUser {
  private readonly toastr = inject(ToastrService);
  private readonly avistamientoService = inject(AvistamientoService);
  private readonly authStore = inject(AuthStoreService);

  idUsuarioToBy= input.required<number>()

  public avistamientoState = signal<AvistamientoState>({
    data: [],
    status: ApiStatus.INIT,
    error:null
  });


  public isLoading = computed(() => this.avistamientoState().status == ApiStatus.LOADING);
  public isError = computed(() => this.avistamientoState().status ==ApiStatus.ERROR);

  public isSuccess = computed(() => this.avistamientoState().status ==ApiStatus.SUCCESS);

  public isNotFound = computed(() => this.avistamientoState().status ==ApiStatus.NOT_FOUND);


  public avistamientos = computed(() => this.avistamientoState().data);



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
    this.avistamientoState.set({...this.avistamientoState(), status:ApiStatus.LOADING})

    this.avistamientoService.getFiltered({usuarioId: this.idUsuarioToBy()})
       .subscribe({
        next: (resp) => {
          if(resp.length >0){

                this.avistamientoState.update((state) => ({
                ...state,
                data: resp,
                status:ApiStatus.SUCCESS,

              }));
          }else{
              this.avistamientoState.set({...this.avistamientoState(), data:[],status:ApiStatus.NOT_FOUND})
          }
        },
        error: (errorMessage) => {
          this.avistamientoState.set({...this.avistamientoState(), status:ApiStatus.LOADING})
          this.avistamientoState.update((state) => ({
                ...state,
                data: [],
                status:ApiStatus.ERROR,
                error: errorMessage,
              }));
          this.toastr.error('Error al recuperar el perfil', 'Error en el servidor');
        },
      })
  }

    getSeverity(agradecimiento: boolean): 'success' | 'info' {
    return agradecimiento ? 'success' : 'info';
  }
}
